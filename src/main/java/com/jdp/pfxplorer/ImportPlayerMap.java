package com.jdp.pfxplorer;

import com.jdp.pfxplorer.dao.PitcherDao;
import com.jdp.pfxplorer.domain.Pitcher;
import com.jdp.pfxplorer.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to import pitcher ids and names from a CSV file, or the default URL.
 * Expects a CSV with the first two columns player_id,name.
 */
@Component
public class ImportPlayerMap {

    private static final String DEFAULT_PLAYER_MAP_URL = "http://crunchtimebaseball.com/master.csv";

    @Autowired
    private PitcherDao pitcherDao;

    public void run(String[] args) {
        List<String> fileLines;

        if (args.length != 2) {
            System.out.println("Import pitchers: filename not specified, trying default url " + DEFAULT_PLAYER_MAP_URL);

            String data = Utils.readUrl(DEFAULT_PLAYER_MAP_URL);
            if (data == null) {
                System.out.println("Could not read player information from " + DEFAULT_PLAYER_MAP_URL + "!");
                return;
            }

            fileLines = new ArrayList<>();
            String[] lines = data.split("\n");
            for (String line : lines) {
                fileLines.add(line);
            }
        } else {
            try {
                fileLines = Files.readAllLines(Paths.get(args[1]), Charset.forName("ISO-8859-1"));
            } catch (IOException e) {
                System.out.println("Unable to read file " + args[1]);
                e.printStackTrace();
                return;
            }
        }

        pitcherDao.deleteAll();

        int ct = 0;
        System.out.print("Importing pitcher ids...");
        for (int i=1; i<fileLines.size(); i++) {
            String[] parts = fileLines.get(i).split(",");
            if (parts[2].equals("P")) {
                Integer playerId = Integer.parseInt(parts[0]);
                String name = parts[1];

                Pitcher pitcher = new Pitcher();
                pitcher.setPitcherId(playerId);
                pitcher.setName(name);

                pitcherDao.save(pitcher);
                ct++;
            }
        }
        System.out.println(ct + " imported");
    }
}
