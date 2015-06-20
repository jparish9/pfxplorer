package com.jdp.pfxplorer;

import com.jdp.pfxplorer.dao.PitcherDao;
import com.jdp.pfxplorer.domain.Pitcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class to import pitcher ids and names from a CSV file.
 */
@Component
public class ImportPlayerMap {

    @Autowired
    private PitcherDao pitcherDao;

    public void run(String[] args) {
        if (args.length != 2) {
            System.out.println("Import pitchers: missing filename");
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(args[1]), Charset.forName("ISO-8859-1"));
        } catch (IOException e) {
            System.out.println("Unable to read file " + args[1]);
            e.printStackTrace();
            return;
        }

        pitcherDao.deleteAll();

        int ct = 0;
        System.out.print("Importing pitcher ids...");
        for (int i=1; i<lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
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
