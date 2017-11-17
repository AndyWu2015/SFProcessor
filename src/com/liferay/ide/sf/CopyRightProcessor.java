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
public class CopyRightProcessor {

	public static void main(String[] args) {

		String baseDir = "/home/andy/dev java/projects/liferay-ide/tools/plugins/com.liferay.ide.upgrade.core/src";
		//String baseDir = "/home/andy/temp/testcr";

		CopyRightProcessor processor = new CopyRightProcessor();
		
		List<String> result = new ArrayList<String>();

		processor.getFileList(baseDir, result);
		
		for(String str : result) 
		{
			System.out.println(processor.processCopyright(str)+":"+str);
		}
		
		System.out.println(result.size());

	}
	
	public void getFileList(String baseDir, List<String> result)
	{
		File file = new File(baseDir);
		
		File[] files = file.listFiles();
		for(File child : files) {
			if(child.isDirectory()) 
			{
				getFileList(child.getAbsolutePath(),result);
			}
			else if(child.getName().endsWith(".java"))
			{
				result.add(child.getAbsolutePath());
			}
		}
	}

	public boolean processCopyright(String file) {

		boolean result = true;

		try {
			// read file content from file

			StringBuffer sb = new StringBuffer("");

			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			String str = null;

			int currentLine = 0;
			boolean startAppend = false;
			boolean finished = false;

			while ((str = br.readLine()) != null) {

				++currentLine;

				if (currentLine == 1 && !str.equals("/**")) {
					br.close();
					reader.close();
					return false;
				}

				if (str.equals(" */") && currentLine < 20) {
					startAppend = true;
				}

				if (startAppend) {
					if (str.equals(" */") && !finished) {

						sb.append("/**\n");
						sb.append(" * Copyright (c) 2000-present Liferay, Inc. All rights reserved.\n");
						sb.append(" *\n");
						sb.append(" * This library is free software; you can redistribute it and/or modify it under\n");
						sb.append(" * the terms of the GNU Lesser General Public License as published by the Free\n");
						sb.append(" * Software Foundation; either version 2.1 of the License, or (at your option)\n");
						sb.append(" * any later version.\n");
						sb.append(" *\n");
						sb.append(" * This library is distributed in the hope that it will be useful, but WITHOUT\n");
						sb.append(" * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS\n");
						sb.append(" * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more\n");
						sb.append(" * details.\n");
						sb.append(" */\n");
						
						finished = true;

					} else {
						sb.append(str + "\n");
					}
				}
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

		return result;
	}

}
