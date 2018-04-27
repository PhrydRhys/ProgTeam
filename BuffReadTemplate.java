/*
 * Just a simple template to utilize BufferedReader
 */

import java.io.*;
import java.util.*;
import java.math.*;

public class BuffReadTemplate {	

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = br.readLine();

		while (line != null) {
			String[] arr = line.split(" ");
			int a = Integer.parseInt(arr[0]);
			
			
		}

	}
}
