package URTorrent;

import java.io.*;
import java.security.*;
import java.util.*;

import URTorrent.URBencode.*;
import URTorrent.URMessage.*;

public class Test {
	public static void main(String args[]) {
		URPeer peer = new URPeer("6969", "UR.mp3.torrent");
		try {
			peer.handshake(peer.metainfo.getInfo_hash());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
