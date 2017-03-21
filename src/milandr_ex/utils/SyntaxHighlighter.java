package milandr_ex.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

/**
 * Created by lizard2k1 on 28.02.2017.
 *
 * Stolen from: https://github.com/TomasMikula/RichTextFX
 * https://github.com/TomasMikula/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywords.java
 */
public class SyntaxHighlighter {
	private SyntaxHighlighter() {
	}
	private static SyntaxHighlighter instance;
	private static final String[] WARNINGS = new String[] {
			"warn", "warni", "warnin", "warning", "alar", "alarm",
	};
	private static final String[] KEYWORDS = new String[] {
			"out", "err", "define",
			"abstract", "assert", "boolean", "break", "byte",
			"case", "catch", "char", "class", "const",
			"continue", "default", "do", "double", "else",
			"enum", "extends", "final", "finally", "float",
			"for", "goto", "if", "implements", "import",
			"instanceof", "int", "interface", "long", "native",
			"new", "package", "private", "protected", "public",
			"return", "short", "static", "strictfp", "super",
			"switch", "synchronized", "this", "throw", "throws",
			"transient", "try", "void", "volatile", "while"
	};

	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String WARNING_PATTERN = "\\b(" + String.join("|", WARNINGS) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

	private static final Pattern PATTERN = Pattern.compile(
			"(?<KEYWORD>" + KEYWORD_PATTERN + ")"
			+ "|(?<PAREN>" + PAREN_PATTERN + ")"
			+ "|(?<BRACE>" + BRACE_PATTERN + ")"
			+ "|(?<BRACKET>" + BRACKET_PATTERN + ")"
			+ "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
			+ "|(?<STRING>" + STRING_PATTERN + ")"
			+ "|(?<COMMENT>" + COMMENT_PATTERN + ")"
	);
	private static final Pattern WARN_PATT = Pattern.compile(
			"(?<WARNING>" + WARNING_PATTERN + ")"
	);

	private Parent coder;
	private Parent getCoder(Scene scene) {
		if (coder == null) coder = newCoder(scene);
		return coder;
	}
	private CodeArea codeArea;

	public CodeArea getCodeArea() {
		return codeArea;
	}

	public SyntaxHighlighter setCode(List<String> codeBlock) {
		codeArea.clear();
		String codeStr = String.join("\n",(codeBlock.toArray(new String[]{})));
//		String codeStr = String.valueOf(codeBlock).replaceAll("[\\[\\]\\,]", "");
		codeArea.replaceText(0, 0, codeStr);
		return this;
	}

	private Parent newCoder(Scene scene) {
		codeArea = new CodeArea();
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

		codeArea.richChanges()
				.filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
				.subscribe(change -> {
					codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
				});
		codeArea.replaceText(0, 0, "");
		scene.getStylesheets().add(getClass().getClassLoader()
				.getResource("milandr_ex/css/java-keywords.css").toExternalForm());
		VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
		StackPane stackPane = new StackPane(new AnchorPane(scrollPane));
		GuiUtils.setAnchors(1.0, scrollPane, stackPane);
		return stackPane;
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder
				= new StyleSpansBuilder<>();
		while(matcher.find()) {
			String styleClass =
					matcher.group("KEYWORD") != null ? "keyword" :
					matcher.group("PAREN") != null ? "paren" :
					matcher.group("BRACE") != null ? "brace" :
					matcher.group("BRACKET") != null ? "bracket" :
					matcher.group("SEMICOLON") != null ? "semicolon" :
					matcher.group("STRING") != null ? "string" :
					matcher.group("COMMENT") != null ? "comment" :
					null; /* never happens */ assert styleClass != null;
			styleClass = findWarningInComments(text, matcher, lastKwEnd, styleClass);
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	private static String findWarningInComments(String text, Matcher matcher, int lastKwEnd, String styleClass) {
		if (styleClass.equals("comment")) {
			String strLine = text.substring(lastKwEnd, matcher.end());
			Matcher mtch = WARN_PATT.matcher(strLine);
			if (mtch.find() && mtch.group("WARNING") != null) {
				styleClass = "warning";
			}
		}
		return styleClass;
	}

	public static Parent get(Scene scene) {
		if (instance == null) instance = new SyntaxHighlighter();
		return instance.getCoder(scene);
	}

	public static SyntaxHighlighter set(Scene scene, List<String> code) {
		get(scene);
		return instance.setCode(code);
	}
}