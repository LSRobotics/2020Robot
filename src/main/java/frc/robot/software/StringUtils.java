package frc.robot.software;

public class StringUtils {
	public static String indent(String in) {
		return in.replaceAll("\n", "\n    ");
	}
}
