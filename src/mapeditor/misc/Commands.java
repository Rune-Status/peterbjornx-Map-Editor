package mapeditor.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Commands implements Runnable {

	@Override
	public void run() {
		BufferedReader inStream = new BufferedReader(new InputStreamReader(
				System.in));
		while (true) {
			try {
				/** Reads the String line. **/
				String line = inStream.readLine();
				String playerCommand = line;
				
				try {
					String args[] = playerCommand.split(",");
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					System.out.println("Map region: " + ObjectscapeLogic.getObjectscapeID(x, y));
					
				} catch (Exception e) {
					
				}
				if (playerCommand.startsWith("exit") || playerCommand.startsWith("quit") || playerCommand.startsWith("stop")) {
					System.out.print("Terminating...");
					System.exit(0);
				} else {
					System.out
					.println("\'"
							+ playerCommand
							+ "\' is not recognized as a valid console command.");
					System.out.println();
				}

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
