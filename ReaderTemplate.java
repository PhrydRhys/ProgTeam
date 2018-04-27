/*
 * Template for handling standard I/O
 * Uses BufferedReader, so 
 * 
 * Referenced from:
 * https://www.geeksforgeeks.org/fast-io-in-java-in-competitive-programming/
 */ 

import java.io.*;
import java.util.*;
import java.math.*;

public class ReaderTemplate {
	
	public static void main(String[] args) throws IOException {
		Reader.read(System.in);

	}
	
	// ------------------------------------------------------------
	
	static class Reader {
		static BufferedReader reader;
		static StringTokenizer tokenizer;
		static StringTokenizer tokenizerLine;

		static void read(InputStream input) {
			reader = new BufferedReader(new InputStreamReader(input));
			tokenizer = new StringTokenizer("");
			tokenizerLine = new StringTokenizer("\n");
		}

		static String next() throws IOException {
			while (!tokenizer.hasMoreTokens()) {
				tokenizer = new StringTokenizer(reader.readLine());
			}
			return tokenizer.nextToken();
		}

		static String nextLine() throws IOException {
			while (!tokenizer.hasMoreTokens()) {
				tokenizerLine = new StringTokenizer(reader.readLine());
			}
			return tokenizerLine.nextToken();
		}

		static char nextChar() throws IOException {
			return next().charAt(0);
		}
		
		static int nextInt() throws IOException {
			return Integer.parseInt(next());
		}

		static double nextDouble() throws IOException {
			return Double.parseDouble(next());
		}

		static long nextLong() throws IOException {
			return Long.parseLong(next());
		}
	}

}
