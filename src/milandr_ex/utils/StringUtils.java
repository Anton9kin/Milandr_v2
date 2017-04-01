package milandr_ex.utils;

import milandr_ex.data.Constants;

import java.io.File;
import java.util.List;

/**
 * Created by lizard on 20.03.17 at 16:07.
 */
public class StringUtils {
	public static int countMatches(String str, String sub){
		int count = 0;
		while (str.contains(sub)){
			str = str.replaceFirst(sub, "");
			count++;
		}
		return count ;
	}
	public static boolean strHasAnySubstr(String str, String... substrs) {
		for(String substr: substrs) {
			if (str.contains(substr)) return true;
		}
		return false;
	}

	static String eng = "qwertyuiop[]asdfghjkl;'zxcvbnm,.?";
	static String rus = "йцукенгшщзхъфывапролджэячсмитьбю.";
	public static void main(String[] args) {
		if (args.length < 1) return;
		String fname = args[0];
		List<String> text = Constants.loadTxtStrings(new File(fname));
		for(String line: text) {
			String[] words = line.split("\\s");
			if (words.length < 1) continue;
			StringBuilder outLine = new StringBuilder();
			if (line.startsWith("--->")) {
				System.out.println(line);
				continue;
			}
			String lastWord = "";
			for(String word: words) {
				if (word == null) continue;
				if (word.isEmpty()) continue;
				if (word.equals(lastWord)) continue;
				lastWord = word;
				if (word.matches("\\[.+]")) {
					outLine.append(word);
					continue;
				}
				outLine.append(word);
//                outLine.append(parseWord(word));
			}
			if (outLine.toString().trim().isEmpty()) continue;
			System.out.println(outLine);
		}
	}

	private static String parseWord(String word) {
		String outWord = word;
		for(int i = 0; i < eng.length(); i++) {
			String regex = eng.charAt(i) + "";
			if (!word.contains(regex)) continue;
			if (regex.equals("[") || regex.equals(".")
					|| regex.equals("?")) regex = "\\" + regex;
			outWord = outWord.replaceAll(regex, "" + rus.charAt(i)) + " ";
		}
		return outWord.replaceAll("\\s+", " ");
	}
}
