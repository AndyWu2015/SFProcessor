package com.liferay.ide.sf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy Wu
 */
public class AddLineProcessor {

	public static void main(String[] args) {

		String inputFile = "/home/andy/input.txt";
		String sfBaseDir = "/home/andy/dev java/projects/liferay-ide/build/com.liferay.ide.build.source.formatter";

		AddLineProcessor processor = new AddLineProcessor();

		SfResult result = processor.parserInput(inputFile, sfBaseDir);

		for (String key : result.getMap().keySet()) {
			List<Integer> values = result.getMap().get(key);

			processor.addLine(key, values.toArray(new Integer[0]));

			System.out.println("finished->" + key);
		}
	}

	public SfResult parserInput(String file, String baseDir) {
		SfResult result = new SfResult();
		try {
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String str = null;

			while ((str = br.readLine()) != null) {
				// System.out.println(str);

				if (str.indexOf(".java") > 0) {
					int lineNumber = Integer.parseInt(str.substring(str.indexOf(".java") + 6, str.length()));
					// System.out.println(lineNumber);
					File base = new File(baseDir);
					Path baseFile = base.toPath();

					String path = str.substring(str.indexOf(":") + 1, str.indexOf(".java") + 6).trim();
					String finalPath = baseFile.resolve(path).toFile().getCanonicalPath();

					result.put(finalPath, lineNumber);
				}
			}

			br.close();
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public class SfResult {
		private Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

		public Map<String, List<Integer>> getMap() {
			return map;
		}

		public void put(String key, Integer value) {
			if (map.keySet().contains(key)) {
				List<Integer> values = (List<Integer>) map.get(key);
				values.add(value);
			} else {
				List<Integer> values = new ArrayList<Integer>();
				values.add(value);
				map.put(key, values);
			}
		}

	}

	public void addLine(String file, Integer[] lineNumbers) {

		try {
			// read file content from file

			StringBuffer sb = new StringBuffer("");

			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			String str = null;

			/*
			 * for(int i=0; i < lineNumber; i++) { if(i == (lineNumber -1)) { result =
			 * br.readLine(); } else { br.readLine(); } }
			 */

			int currentLine = 0;
			int cursor = 0;
			int size = lineNumbers.length;

			while ((str = br.readLine()) != null) {

				++currentLine;

				if (cursor < size && currentLine == lineNumbers[cursor]) {
					cursor++;

					sb.append("\n");
				}

				sb.append(str + "\n");

				// System.out.println(str);
			}

			br.close();
			reader.close();

			// write string to file
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(sb.toString());

			bw.close();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
