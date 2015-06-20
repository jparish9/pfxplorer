package com.jdp.pfxplorer;

import com.jdp.pfxplorer.dao.PitchDao;
import com.jdp.pfxplorer.domain.Pitch;
import com.jdp.pfxplorer.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to import Pitch F/X data.
 * Duplicate checking is performed when importing,
 * this utility is safe to run multiple times over the same date range.
 *
 * Usage: import [year] [month]
 * (Example: import 2014 9 will import all pitches from September 2014.)
 * If month is omitted, all pitches from that year will be imported.
 * If month and year are omitted, all pitches from 2008-2015 will be imported.
 */
@Component
public class ImportPFX {

    private static final String BASE_URL = "http://gd2.mlb.com/components/game/mlb/";

    @Autowired
    private PitchDao pitchDao;

    private XPath xpath;

    public void run(String... args) throws Exception {
        // parse arguments, set year and month ranges to import.
        int startYear = 2008;
        int endYear = 2015;
        int startMonth = 3;     // all pitches fall inside march-october
        int endMonth = 10;
        if (args.length >= 2) {
            Integer year = parseIntegerArgument(args[1]);
            if (year == null) {
                showUsage();
                return;
            }

            // fixed year
            startYear = endYear = Integer.parseInt(args[1]);
        }
        if (args.length >= 3) {
            Integer month = parseIntegerArgument(args[2]);
            if (month == null) {
                showUsage();
                return;
            }

            // fixed month
            startMonth = endMonth = Integer.parseInt(args[2]);
        }

        XPathFactory xpf = XPathFactory.newInstance();
        xpath = xpf.newXPath();

        // precompile regex's for searching the game list html pages.
        Pattern gidPattern = Pattern.compile("<a href=\"gid_(.*)/\"");          // e.g. "gid_2008_04_01_anamlb_minmlb_1"
        Pattern dayPattern = Pattern.compile("<a href=\"day_([0-9]+)/\"");      // e.g. "day_12"

        for (int year = startYear; year <= endYear; year++) {
            for (int month = startMonth; month <= endMonth; month++) {

                String mlb = readUrl(BASE_URL + "year_" + year + "/month_" + pad(month, 2));
                if (mlb == null) {
                    System.out.println("*** CAN'T FIND MONTH: " + year + " - " + month);
                    continue;
                }

                Matcher dayMatcher = dayPattern.matcher(mlb);

                while (dayMatcher.find()) {
                    String day = dayMatcher.toMatchResult().group(1);

                    String daymlb = readUrl(BASE_URL + "year_" + year + "/month_" + pad(month, 2) + "/day_" + day);

                    Matcher gidMatcher = gidPattern.matcher(daymlb);

                    while (gidMatcher.find()) {
                        String gameId = gidMatcher.toMatchResult().group(1);

                        System.out.print("Process game " + gameId + ": ");

                        if (pitchDao.countByGameId(gameId) > 0) {
                            System.out.println("*** Skipping, existing pitches found for game ***");
                            continue;
                        }

                        String gameData = readUrl(BASE_URL + "year_" + year + "/month_" + pad(month, 2) + "/day_" + day + "/gid_" + gameId + "/game.xml");
                        if (gameData == null) {
                            System.out.println("*** No Game XML ***");
                            continue;
                        }

                        Document gameDataDoc = XmlUtil.parseXml(gameData);
                        String gameType = (String) xpath.evaluate("//game/@type", gameDataDoc, XPathConstants.STRING);

                        if (gameType == null || !gameType.equals("R")) {
                            System.out.println("*** Invalid Game Type " + gameType + " ***");
                            continue;
                        }

                        String inningXml = readUrl(BASE_URL + "year_" + year + "/month_" + pad(month, 2) + "/day_" + day + "/gid_" + gameId + "/inning/inning_all.xml");
                        if (inningXml == null) {
                            System.out.println("*** No Innings Found ***");
                            continue;
                        }

                        processInnings(inningXml, year, month, Integer.parseInt(day, 10), gameId);
                        System.out.println("");
                    }
                }
            }
        }
    }

    /**
     * Helper method to read a url directly into a string.
     * @return contents of url as a string, or null if there was an error.
     */
    private static String readUrl(String urlString) {
        InputStream urlStream;
        try {
            urlStream = new URL(urlString).openStream();
        } catch (IOException e) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                urlStream.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    /**
     * Utility function to left-pad a number with zeros.
     * e.g. pad(5, 2) --> "05"
     **/
    private String pad(int value, int size) {
        String val = String.valueOf(value);
        while (val.length() < size)
            val = "0" + val;

        return val;
    }

    /**
     * Process an individual game's xml into discrete database records for each pitch.
     **/
    private void processInnings(String inningXml, int year, int month, int day, String gameId) throws Exception {
        int pitchCountForGame = 0;

        Document doc = XmlUtil.parseXml(inningXml);
        // look for all complete pitch nodes (those with a required pitch attribute)
        NodeList nl = (NodeList) xpath.evaluate("//game/inning/*/atbat/pitch[@break_y]", doc, XPathConstants.NODESET);

        if (nl.getLength() == 0) {
            System.out.print("*** No PFX Innings Found ***");
            return;
        }

        // iterate through innings
        nl = (NodeList) xpath.evaluate("//game/inning", doc, XPathConstants.NODESET);

        for (int a = 0; a < nl.getLength(); a++) {
            Node inningNode = nl.item(a);

            int inning = parseIntegerNode(inningNode, "num");

            // iterate through at-bats in the inning
            NodeList atbatNodeList = (NodeList) xpath.evaluate("*/atbat", inningNode, XPathConstants.NODESET);

            for (int i = 0; i < atbatNodeList.getLength(); i++) {
                Node n = atbatNodeList.item(i);

                int batterId = parseIntegerNode(n, "batter");
                String batterHitting = parseStringNode(n, "stand");
                String batterHeight = parseStringNode(n, "b_height");
                String pitcherThrows = parseStringNode(n, "p_throws");
                int pitcherId = parseIntegerNode(n, "pitcher");

                int ballCount = 0;
                int strikeCount = 0;

                // iterate through pitches in the at-bat
                NodeList nl2 = (NodeList) xpath.evaluate("pitch", n, XPathConstants.NODESET);

                for (int j = 0; j < nl2.getLength(); j++) {
                    Node n2 = nl2.item(j);

                    String type = n2.getAttributes().getNamedItem("type").getTextContent();

                    // ignore <pitch> nodes that don't appear to be valid (missing required attributes)
                    if (n2.getAttributes().getNamedItem("break_y") != null) {
                        Pitch pitch = new Pitch();
                        pitch.setInning(inning);
                        pitch.setBatterId(batterId);
                        pitch.setBatterHitting(batterHitting);
                        pitch.setBatterHeight(batterHeight);
                        pitch.setPitcherId(pitcherId);
                        pitch.setPitcherThrows(pitcherThrows);
                        pitch.setCountBalls(ballCount);
                        pitch.setCountStrikes(strikeCount);
                        pitch.setYear(year);
                        pitch.setMonth(month);
                        pitch.setDay(day);
                        pitch.setGameId(gameId);
                        pitch.setDescription(n2.getAttributes().getNamedItem("des").getTextContent());
                        pitch.setType(type);
                        pitch.setX(parseDoubleNode(n2, "x"));
                        pitch.setY(parseDoubleNode(n2, "y"));
                        pitch.setStartSpeed(parseDoubleNode(n2, "start_speed"));
                        pitch.setEndSpeed(parseDoubleNode(n2, "end_speed"));
                        pitch.setSzTop(parseDoubleNode(n2, "sz_top"));
                        pitch.setSzBottom(parseDoubleNode(n2, "sz_bot"));
                        pitch.setPfxX(parseDoubleNode(n2, "pfx_x"));
                        pitch.setPfxZ(parseDoubleNode(n2, "pfx_z"));
                        pitch.setPx(parseDoubleNode(n2, "px"));
                        pitch.setPz(parseDoubleNode(n2, "pz"));
                        pitch.setX0(parseDoubleNode(n2, "x0"));
                        pitch.setY0(parseDoubleNode(n2, "y0"));
                        pitch.setZ0(parseDoubleNode(n2, "z0"));
                        pitch.setVx0(parseDoubleNode(n2, "vx0"));
                        pitch.setVy0(parseDoubleNode(n2, "vy0"));
                        pitch.setVz0(parseDoubleNode(n2, "vz0"));
                        pitch.setAx(parseDoubleNode(n2, "ax"));
                        pitch.setAy(parseDoubleNode(n2, "ay"));
                        pitch.setAz(parseDoubleNode(n2, "az"));

                        // optional fields (not present in all pitch f/x data)
                        pitch.setSvId(parseStringNode(n2, "sv_id"));
                        pitch.setBreakY(parseDoubleNode(n2, "break_y"));
                        pitch.setBreakAngle(parseDoubleNode(n2, "break_angle"));
                        pitch.setBreakLength(parseDoubleNode(n2, "break_length"));
                        pitch.setPitchType(parseStringNode(n2, "pitch_type"));
                        pitch.setTypeConfidence(parseDoubleNode(n2, "type_confidence"));
                        pitch.setZone(parseIntegerNode(n2, "zone"));
                        pitch.setNasty(parseIntegerNode(n2, "nasty"));
                        pitch.setSpinDir(parseDoubleNode(n2, "spin_dir"));
                        pitch.setSpinRate(parseDoubleNode(n2, "spin_rate"));

                        pitchDao.save(pitch);

                        pitchCountForGame++;
                    }

                    // keep manual track of pitch count to be stored with each entry
                    // (not present in raw data).
                    if (type.equals("B"))
                        ballCount++;
                    else if (type.equals("S") && strikeCount < 2)
                        strikeCount++;
                }
            }
        }

        System.out.print(pitchCountForGame + " pitches imported... ");
    }

    /**
     * Utility functions to parse out numbers or strings from xml nodes.
     */
    private Double parseDoubleNode(Node node, String attributeName) {
        String attrText = getAttributeText(node, attributeName);
        return attrText != null ? Double.parseDouble(attrText) : null;
    }

    private String parseStringNode(Node node, String attributeName) {
        return getAttributeText(node, attributeName);
    }

    private Integer parseIntegerNode(Node node, String attributeName) {
        String attrText = getAttributeText(node, attributeName);
        return attrText != null ? Integer.parseInt(attrText) : null;
    }

    private String getAttributeText(Node node, String attributeName) {
        Node attr = node.getAttributes().getNamedItem(attributeName);
        return attr != null ? attr.getTextContent() : null;
    }

    /**
     * Utility function to parse a command-line argument into an integer.
     */
    private Integer parseIntegerArgument(String arg) {
        Integer val = null;
        try {
            val = Integer.parseInt(arg);
        } catch (NumberFormatException e) {}

        return val;
    }

    private void showUsage() {
        System.out.println("Import usage: \"import [year] [month]\"");
    }
}
