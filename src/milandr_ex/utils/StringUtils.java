package milandr_ex.utils;

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
}
