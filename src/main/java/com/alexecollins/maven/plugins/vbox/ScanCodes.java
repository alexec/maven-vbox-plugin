package com.alexecollins.maven.plugins.vbox;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ScanCodes {
	public static void main(String[] args) {
		final String y = ",,Esc," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x001," +
				"," +
				"1," +
				"," +
				"!," +
				"," +
				"," +
				"," +
				"0x002," +
				"," +
				"2," +
				"," +
				"@," +
				"," +
				"," +
				"," +
				"0x003," +
				"," +
				"3," +
				"," +
				"#," +
				"," +
				"," +
				"," +
				"0x004," +
				"," +
				"4," +
				"," +
				"$," +
				"," +
				"," +
				"," +
				"0x005," +
				"," +
				"5," +
				"," +
				"%," +
				"," +
				"," +
				"," +
				"0x006," +
				"," +
				"6," +
				"," +
				"^," +
				"," +
				"," +
				"," +
				"0x007," +
				"," +
				"7," +
				"," +
				"&," +
				"," +
				"," +
				"," +
				"0x008," +
				"," +
				"8," +
				"," +
				"*," +
				"," +
				"," +
				"," +
				"0x009," +
				"," +
				"9," +
				"," +
				"(," +
				"," +
				"," +
				"," +
				"0x00a," +
				"," +
				"0," +
				"," +
				")," +
				"," +
				"," +
				"," +
				"0x00b," +
				"," +
				"-," +
				"," +
				"_," +
				"," +
				"," +
				"," +
				"0x00c," +
				"," +
				"=," +
				"," +
				"+," +
				"," +
				"," +
				"," +
				"0x00d," +
				"," +
				"Backspace," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x00e," +
				"," +
				"Tab," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x00f," +
				"," +
				"q," +
				"," +
				"Q," +
				"," +
				"," +
				"," +
				"0x010," +
				"," +
				"w," +
				"," +
				"W," +
				"," +
				"," +
				"," +
				"0x011," +
				"," +
				"e," +
				"," +
				"E," +
				"," +
				"," +
				"," +
				"0x012," +
				"," +
				"r," +
				"," +
				"R," +
				"," +
				"," +
				"," +
				"0x013," +
				"," +
				"t," +
				"," +
				"T," +
				"," +
				"," +
				"," +
				"0x014," +
				"," +
				"y," +
				"," +
				"Y," +
				"," +
				"," +
				"," +
				"0x015," +
				"," +
				"u," +
				"," +
				"U," +
				"," +
				"," +
				"," +
				"0x016," +
				"," +
				"i," +
				"," +
				"I," +
				"," +
				"," +
				"," +
				"0x017," +
				"," +
				"o," +
				"," +
				"O," +
				"," +
				"," +
				"," +
				"0x018," +
				"," +
				"p," +
				"," +
				"P," +
				"," +
				"," +
				"," +
				"0x019," +
				"," +
				"[," +
				"," +
				"{," +
				"," +
				"," +
				"," +
				"0x01a," +
				"," +
				"]," +
				"," +
				"}," +
				"," +
				"," +
				"," +
				"0x01b," +
				"," +
				"Enter," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x01c," +
				"," +
				"Ctrl," +
				"," +
				"," +
				"," +
				"left," +
				"," +
				"0x01d," +
				"," +
				"a," +
				"," +
				"A," +
				"," +
				"," +
				"," +
				"0x01e," +
				"," +
				"s," +
				"," +
				"S," +
				"," +
				"," +
				"," +
				"0x01f," +
				"," +
				"d," +
				"," +
				"D," +
				"," +
				"," +
				"," +
				"0x020," +
				"," +
				"f," +
				"," +
				"F," +
				"," +
				"," +
				"," +
				"0x021," +
				"," +
				"g," +
				"," +
				"G," +
				"," +
				"," +
				"," +
				"0x022," +
				"," +
				"h," +
				"," +
				"H," +
				"," +
				"," +
				"," +
				"0x023," +
				"," +
				"j," +
				"," +
				"J," +
				"," +
				"," +
				"," +
				"0x024," +
				"," +
				"k," +
				"," +
				"K," +
				"," +
				"," +
				"," +
				"0x025," +
				"," +
				"l," +
				"," +
				"L," +
				"," +
				"," +
				"," +
				"0x026," +
				"," +
				";," +
				"," +
				":," +            // hacked
				"," +
				"," +
				"," +
				"0x027," +
				"," +
				"'," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x028," +
				"," +
				"`," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x029," +
				"," +
				"Shift," +
				"," +
				"," +
				"," +
				"left," +
				"," +
				"0x02a," +
				"," +
				"\\," +
				"," +
				"|," +
				"," +
				"," +
				"," +
				"0x02b," +
				"," +
				"z," +
				"," +
				"Z," +
				"," +
				"," +
				"," +
				"0x02c," +
				"," +
				"x," +
				"," +
				"X," +
				"," +
				"," +
				"," +
				"0x02d," +
				"," +
				"c," +
				"," +
				"C," +
				"," +
				"," +
				"," +
				"0x02e," +
				"," +
				"v," +
				"," +
				"V," +
				"," +
				"," +
				"," +
				"0x02f," +
				"," +
				"b," +
				"," +
				"B," +
				"," +
				"," +
				"," +
				"0x030," +
				"," +
				"n," +
				"," +
				"N," +
				"," +
				"," +
				"," +
				"0x031," +
				"," +
				"m," +
				"," +
				"M," +
				"," +
				"," +
				"," +
				"0x032," +
				"," +
				",," +
				"," +
				"<," +
				"," +
				"," +
				"," +
				"0x033," +
				"," +
				".," +
				"," +
				">," +
				"," +
				"," +
				"," +
				"0x034," +
				"," +
				"/," +
				"," +
				"?," +
				"," +
				"," +
				"," +
				"0x035," +
				"," +
				"Shift," +
				"," +
				"," +
				"," +
				"right," +
				"," +
				"0x036," +
				"," +
				"*," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x037," +
				"," +
				"Alt," +
				"," +
				"," +
				"," +
				"left," +
				"," +
				"0x038," +
				"," +
				" ," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x039," +
				"," +
				"Caps Lock," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x03a," +
				"," +
				"F1," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x03b," +
				"," +
				"F2," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x03c," +
				"," +
				"F3," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x03d," +
				"," +
				"F4," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x03e," +
				"," +
				"F5," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x03f," +
				"," +
				"F6," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x040," +
				"," +
				"F7," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x041," +
				"," +
				"F8," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x042," +
				"," +
				"F9," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x043," +
				"," +
				"F10," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x044," +
				"," +
				"Num Lock," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x045," +
				"," +
				"Scroll Lock," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x046," +
				"," +
				"Home," +
				"," +
				"7," +
				"," +
				"numeric pad," +
				"," +
				"0x047," +
				"," +
				"Up arrow," +
				"," +
				"8," +
				"," +
				"numeric pad," +
				"," +
				"0x048," +
				"," +
				"PgUp," +
				"," +
				"9," +
				"," +
				"numeric pad," +
				"," +
				"0x049," +
				"," +
				"-," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x04a," +
				"," +
				"Left arrow," +
				"," +
				"4," +
				"," +
				"numeric pad," +
				"," +
				"0x04b," +
				"," +
				"5," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x04c," +
				"," +
				"Right arrow," +
				"," +
				"6," +
				"," +
				"numeric pad," +
				"," +
				"0x04d," +
				"," +
				"+," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x04e," +
				"," +
				"End," +
				"," +
				"1," +
				"," +
				"numeric pad," +
				"," +
				"0x04f," +
				"," +
				"Down arrow," +
				"," +
				"2," +
				"," +
				"numeric pad," +
				"," +
				"0x050," +
				"," +
				"PgDn," +
				"," +
				"3," +
				"," +
				"numeric pad," +
				"," +
				"0x051," +
				"," +
				"Ins," +
				"," +
				"0," +
				"," +
				"numeric pad," +
				"," +
				"0x052," +
				"," +
				"Del," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x053," +
				"," +
				"F11," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x057," +
				"," +
				"F12," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x058," +
				"," +
				"Break," +
				"," +
				"Pause," +
				"," +
				"," +
				"," +
				"0x100," +
				"," +
				"Enter," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x11c," +
				"," +
				"Ctrl," +
				"," +
				"," +
				"," +
				"right," +
				"," +
				"0x11d," +
				"," +
				"/," +
				"," +
				"," +
				"," +
				"numeric pad," +
				"," +
				"0x135," +
				"," +
				"SysRq," +
				"," +
				"Print Scrn," +
				"," +
				"," +
				"," +
				"0x137," +
				"," +
				"Alt," +
				"," +
				"," +
				"," +
				"right," +
				"," +
				"0x138," +
				"," +
				"Home," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x147," +
				"," +
				"Up arrow," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x148," +
				"," +
				"Page Up," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x149," +
				"," +
				"Left arrow," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x14b," +
				"," +
				"Right arrow," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x14d," +
				"," +
				"End," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x14f," +
				"," +
				"Down arrow," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x150," +
				"," +
				"Page Down," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x151," +
				"," +
				"Insert," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x152," +
				"," +
				"Delete," +
				"," +
				"," +
				"," +
				"function pad," +
				"," +
				"0x153," +
				"," +
				"Windows," +
				"," +
				"," +
				"," +
				"left," +
				"," +
				"0x15b," +
				"," +
				"Windows," +
				"," +
				"," +
				"," +
				"right," +
				"," +
				"0x15c," +
				"," +
				"Menu," +
				"," +
				"," +
				"," +
				"," +
				"," +
				"0x15d,,";


		for (int c = 1; c < 0xff; c++) {
			final String s = "0x0" + (c < 0x10 ? "0" : "") + Integer.toHexString(c).toLowerCase();

			final Matcher m = Pattern.compile("([^,]*),,([^,]*),,,," + s).matcher(y);
			if (m.find()) {
				System.out.println("// " + m.group());
				System.out.println("x.put(\"" + m.group(1) + "\", new Integer[]{" + c + "," + (c + 0x80) + "});");
				if (m.group(2).length() > 0)
					System.out.println("x.put(\"" + m.group(2) + "\", new Integer[]{" + 0x02a + "," + c + "," + (c + 0x80) + "," + (0x80 + 0x02a) + "});");
			} else {
				System.out.println("// TODO  - ignoring code " + s);
			}
		}
	}

	public static int[] forString(final String string) {
		final List<Integer> sc = new ArrayList<Integer>();

		for (char c : string.toCharArray()) {
			for (int s : forKey(String.valueOf(c))) {
				sc.add(s);
			}
		}

		return ArrayUtils.toPrimitive(sc.toArray(new Integer[]{sc.size()}));
	}

	private static final Map<String, Integer[]> x = new HashMap<String, Integer[]>();

	static {
// Esc,,,,,,0x001
		x.put("Esc", new Integer[]{1, 129});
// 1,,!,,,,0x002
		x.put("1", new Integer[]{2, 130});
		x.put("!", new Integer[]{42, 2, 130, 170});
// 2,,@,,,,0x003
		x.put("2", new Integer[]{3, 131});
		x.put("@", new Integer[]{42, 3, 131, 170});
// 3,,#,,,,0x004
		x.put("3", new Integer[]{4, 132});
		x.put("#", new Integer[]{42, 4, 132, 170});
// 4,,$,,,,0x005
		x.put("4", new Integer[]{5, 133});
		x.put("$", new Integer[]{42, 5, 133, 170});
// 5,,%,,,,0x006
		x.put("5", new Integer[]{6, 134});
		x.put("%", new Integer[]{42, 6, 134, 170});
// 6,,^,,,,0x007
		x.put("6", new Integer[]{7, 135});
		x.put("^", new Integer[]{42, 7, 135, 170});
// 7,,&,,,,0x008
		x.put("7", new Integer[]{8, 136});
		x.put("&", new Integer[]{42, 8, 136, 170});
// 8,,*,,,,0x009
		x.put("8", new Integer[]{9, 137});
		x.put("*", new Integer[]{42, 9, 137, 170});
// 9,,(,,,,0x00a
		x.put("9", new Integer[]{10, 138});
		x.put("(", new Integer[]{42, 10, 138, 170});
// 0,,),,,,0x00b
		x.put("0", new Integer[]{11, 139});
		x.put(")", new Integer[]{42, 11, 139, 170});
// -,,_,,,,0x00c
		x.put("-", new Integer[]{12, 140});
		x.put("_", new Integer[]{42, 12, 140, 170});
// =,,+,,,,0x00d
		x.put("=", new Integer[]{13, 141});
		x.put("+", new Integer[]{42, 13, 141, 170});
// Backspace,,,,,,0x00e
		x.put("Backspace", new Integer[]{14, 142});
// Tab,,,,,,0x00f
		x.put("Tab", new Integer[]{15, 143});
// q,,Q,,,,0x010
		x.put("q", new Integer[]{16, 144});
		x.put("Q", new Integer[]{42, 16, 144, 170});
// w,,W,,,,0x011
		x.put("w", new Integer[]{17, 145});
		x.put("W", new Integer[]{42, 17, 145, 170});
// e,,E,,,,0x012
		x.put("e", new Integer[]{18, 146});
		x.put("E", new Integer[]{42, 18, 146, 170});
// r,,R,,,,0x013
		x.put("r", new Integer[]{19, 147});
		x.put("R", new Integer[]{42, 19, 147, 170});
// t,,T,,,,0x014
		x.put("t", new Integer[]{20, 148});
		x.put("T", new Integer[]{42, 20, 148, 170});
// y,,Y,,,,0x015
		x.put("y", new Integer[]{21, 149});
		x.put("Y", new Integer[]{42, 21, 149, 170});
// u,,U,,,,0x016
		x.put("u", new Integer[]{22, 150});
		x.put("U", new Integer[]{42, 22, 150, 170});
// i,,I,,,,0x017
		x.put("i", new Integer[]{23, 151});
		x.put("I", new Integer[]{42, 23, 151, 170});
// o,,O,,,,0x018
		x.put("o", new Integer[]{24, 152});
		x.put("O", new Integer[]{42, 24, 152, 170});
// p,,P,,,,0x019
		x.put("p", new Integer[]{25, 153});
		x.put("P", new Integer[]{42, 25, 153, 170});
// [,,{,,,,0x01a
		x.put("[", new Integer[]{26, 154});
		x.put("{", new Integer[]{42, 26, 154, 170});
// ],,},,,,0x01b
		x.put("]", new Integer[]{27, 155});
		x.put("}", new Integer[]{42, 27, 155, 170});
// Enter,,,,,,0x01c
		x.put("Enter", new Integer[]{28, 156});
// TODO  - ignoring code 0x01d
// a,,A,,,,0x01e
		x.put("a", new Integer[]{30, 158});
		x.put("A", new Integer[]{42, 30, 158, 170});
// s,,S,,,,0x01f
		x.put("s", new Integer[]{31, 159});
		x.put("S", new Integer[]{42, 31, 159, 170});
// d,,D,,,,0x020
		x.put("d", new Integer[]{32, 160});
		x.put("D", new Integer[]{42, 32, 160, 170});
// f,,F,,,,0x021
		x.put("f", new Integer[]{33, 161});
		x.put("F", new Integer[]{42, 33, 161, 170});
// g,,G,,,,0x022
		x.put("g", new Integer[]{34, 162});
		x.put("G", new Integer[]{42, 34, 162, 170});
// h,,H,,,,0x023
		x.put("h", new Integer[]{35, 163});
		x.put("H", new Integer[]{42, 35, 163, 170});
// j,,J,,,,0x024
		x.put("j", new Integer[]{36, 164});
		x.put("J", new Integer[]{42, 36, 164, 170});
// k,,K,,,,0x025
		x.put("k", new Integer[]{37, 165});
		x.put("K", new Integer[]{42, 37, 165, 170});
// l,,L,,,,0x026
		x.put("l", new Integer[]{38, 166});
		x.put("L", new Integer[]{42, 38, 166, 170});
// ;,,:,,,,0x027
		x.put(";", new Integer[]{39, 167});
		x.put(":", new Integer[]{42, 39, 167, 170});
// ',,,,,,0x028
		x.put("'", new Integer[]{40, 168});
// `,,,,,,0x029
		x.put("`", new Integer[]{41, 169});
// TODO  - ignoring code 0x02a
// \,,|,,,,0x02b
		x.put("\\", new Integer[]{43, 171});
		x.put("|", new Integer[]{42, 43, 171, 170});
// z,,Z,,,,0x02c
		x.put("z", new Integer[]{44, 172});
		x.put("Z", new Integer[]{42, 44, 172, 170});
// x,,X,,,,0x02d
		x.put("x", new Integer[]{45, 173});
		x.put("X", new Integer[]{42, 45, 173, 170});
// c,,C,,,,0x02e
		x.put("c", new Integer[]{46, 174});
		x.put("C", new Integer[]{42, 46, 174, 170});
// v,,V,,,,0x02f
		x.put("v", new Integer[]{47, 175});
		x.put("V", new Integer[]{42, 47, 175, 170});
// b,,B,,,,0x030
		x.put("b", new Integer[]{48, 176});
		x.put("B", new Integer[]{42, 48, 176, 170});
// n,,N,,,,0x031
		x.put("n", new Integer[]{49, 177});
		x.put("N", new Integer[]{42, 49, 177, 170});
// m,,M,,,,0x032
		x.put("m", new Integer[]{50, 178});
		x.put("M", new Integer[]{42, 50, 178, 170});
// ,,<,,,,0x033
		x.put("", new Integer[]{51, 179});
		x.put("<", new Integer[]{42, 51, 179, 170});
// .,,>,,,,0x034
		x.put(".", new Integer[]{52, 180});
		x.put(">", new Integer[]{42, 52, 180, 170});
// /,,?,,,,0x035
		x.put("/", new Integer[]{53, 181});
		x.put("?", new Integer[]{42, 53, 181, 170});
// TODO  - ignoring code 0x036
// TODO  - ignoring code 0x037
// TODO  - ignoring code 0x038
//  ,,,,,,0x039
		x.put(" ", new Integer[]{57, 185});
// Caps Lock,,,,,,0x03a
		x.put("Caps Lock", new Integer[]{58, 186});
// F1,,,,,,0x03b
		x.put("F1", new Integer[]{59, 187});
// F2,,,,,,0x03c
		x.put("F2", new Integer[]{60, 188});
// F3,,,,,,0x03d
		x.put("F3", new Integer[]{61, 189});
// F4,,,,,,0x03e
		x.put("F4", new Integer[]{62, 190});
// F5,,,,,,0x03f
		x.put("F5", new Integer[]{63, 191});
// F6,,,,,,0x040
		x.put("F6", new Integer[]{64, 192});
// F7,,,,,,0x041
		x.put("F7", new Integer[]{65, 193});
// F8,,,,,,0x042
		x.put("F8", new Integer[]{66, 194});
// F9,,,,,,0x043
		x.put("F9", new Integer[]{67, 195});
// F10,,,,,,0x044
		x.put("F10", new Integer[]{68, 196});
// TODO  - ignoring code 0x045
// Scroll Lock,,,,,,0x046
		x.put("Scroll Lock", new Integer[]{70, 198});
// TODO  - ignoring code 0x047
// TODO  - ignoring code 0x048
// TODO  - ignoring code 0x049
// TODO  - ignoring code 0x04a
// TODO  - ignoring code 0x04b
// TODO  - ignoring code 0x04c
// TODO  - ignoring code 0x04d
// TODO  - ignoring code 0x04e
// TODO  - ignoring code 0x04f
// TODO  - ignoring code 0x050
// TODO  - ignoring code 0x051
// TODO  - ignoring code 0x052
// TODO  - ignoring code 0x053
// TODO  - ignoring code 0x054
// TODO  - ignoring code 0x055
// TODO  - ignoring code 0x056
// F11,,,,,,0x057
		x.put("F11", new Integer[]{87, 215});
// F12,,,,,,0x058
		x.put("F12", new Integer[]{88, 216});
// TODO  - ignoring code 0x059
	}

	public static int[] forKey(String key) {
		final Integer[] array = x.get(key);
		if (array == null) {
			throw new IllegalArgumentException("invalid key <" + key + ">");
		}
		return ArrayUtils.toPrimitive(array);
	}
}
