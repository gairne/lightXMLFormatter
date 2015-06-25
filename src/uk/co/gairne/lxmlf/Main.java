package uk.co.gairne.lxmlf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

import uk.co.gairne.lxmlf.exception.ParserException;
import uk.co.gairne.lxmlf.parser.StaxParser;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException {
		if (args.length >= 1) {
			System.out.println("Processing " + args[0]);
			StaxParser s = new StaxParser();
			String output = s.parseFile(args[0]).toString();
			if (args.length >= 2) {
				System.out.println("Saving output to " + args[1]);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1])));
				out.write(output);
				out.close();
			}
			else {
				int i = 0;
				while (!new File(args[0] + ".bak." + i).exists()) {
					i++;
				}
				System.out.println("Backing up " + args[0] + " to " + args[0] + ".bak." + i);
				Files.move(new File(args[0]).toPath(), new File(args[0]+".bak."+i).toPath());
				System.out.println("Saving output to " + args[0]);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[0])));
				out.write(output);
				out.close();
			}
		}
		else {
			System.err.println("Missing input XML file option");
		}
	}

}
