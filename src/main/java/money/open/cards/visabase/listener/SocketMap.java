/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package money.open.cards.visabase.listener;

import lombok.extern.slf4j.Slf4j;

import java.util.*;


import java.net.*;
import java.util.logging.Logger;

@Slf4j
public class SocketMap {

	public static Map<String, Socket> sckMap = new HashMap<String, Socket>();
	public static Map<String, ServerSocket> serverScoketMap = new HashMap<String, ServerSocket>();
	public static Map<String, ServerSocket> postSckMap = new HashMap<String, ServerSocket>();

	public boolean reset(String ip, int port, String key) {
		int count = 1;
		while (true)// trying for 3 times
		{
			try {
				log.info(key + ":Socket is retrying for connecting  IP::" + ip + ":PORT::" + port
						+ " for Deliverychannel::" + key + " for ::" + count + "::time");
				Socket sc = new Socket(ip, port);
				sckMap.put(key, sc);
				log.info(key + ":Socket retrying for connection is established for IP::" + ip + ":PORT::" + port
						+ " for Deliverychannel::" + key);
				return true;
			} catch (Exception e) {
				count++;
				/* e.printStackTrace(); */
				if (count == 999) {
					log.error("Socket  retrying for connecting  IP::" + ip + ":PORT::" + port + "for Deliverychannel::"
							+ key + " is failed after 3 times");
					log.error(
							"check with Bankserve server.After telnet successful on bankserve kindly restart the TPE appserver for Deliverychannel ::"
									+ key);
					return false;
				}
				// Thread sleep need to be added here
			}
		}

	}

	public boolean resetPostilion(int port, String key) {
		int count = 1;
		while (true)// trying for 3 times
		{
			try {
				System.out.println("Socket is retrying for connecting :PORT::" + port + " for Deliverychannel::" + key
						+ " for ::" + count + "::time");
				ServerSocket sc = new ServerSocket(port);
				postSckMap.put(key, sc);
				System.out.println("Socket retrying for connection is established for :PORT::" + port
						+ " for Deliverychannel::" + key);
				return true;
			} catch (Exception e) {
				count++;
				/* e.printStackTrace(); */
				if (count == 999) {
					System.out.println("Socket  retrying for connecting :PORT::" + port + "for Deliverychannel::" + key
							+ " is failed after 3 times");
					System.out.println(
							"check with Bankserve server.After telnet successful on bankserve kindly restart the TPE appserver for Deliverychannel ::"
									+ key);
					return false;
				}
				// Thread sleep need to be added here
			}
		}

	}

}
