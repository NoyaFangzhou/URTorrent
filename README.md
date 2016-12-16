# URTorrent

1. Our program is a Java program, makefile and README is included
2. Usage: 
   STEP 1: make
   STEP 2: java URTorrent #{torrent_file_name} #{port_number}(port number should beyond 9)
           i.e java urtorrent UR.mp3.torrent 9111
3. There are two main parts in the program:
   (1): peers get .torrent file, parse it to metainfo(object), peer id is (teamName + port), which guarentee the id be unique
	peers connect with tracker with messages in the metainfo, using HTTP Get
	when there is a response from tracker, match first line with "HTTP/1.1 200 OK", if match, success, if not, failed
	get last line as valued trackerresponse, seperate it as two parts, for first part, debencode it, get information like complete, download, upload
	for "peers:", it is a 6-byte binary string, get its first 4 byte as ip address, last 2 bytes is port number
	storing <ip address, port> into a ArrayList, which is a ArrayList<URPeerInfo>
	in this ArrayList, there are two import bolean variable, bolean handshakeflag, bolean[] bitfieldflag
	if a peer had sent handshake with a port, the handshakeflag is set to 1, or it is 0
	In this way, when a peer recieve a handshake, it will check the client's port to see handshake flag, if it is 1,this peer had sent handshake to it before, it won't send handshake again.
	For bitfield, if a piece flag is 1, it means the peer has this piece, it doesn't need it, if it is 0, the peer will request this piece later
   (2): peer connect with peer:
	if peer have file in localhost, it is a seeder, if not, it is a leecher
	if peer is a seeder, it does nothing, wait for leecher's connection; if peer is a leecher, it start connects with peers responded from tracker
	leecher sends handshake with every peers in the ArrayList, when peers handshakeflag is 0, sent handshake.
	then it will receive handshake from peers, and then send bitfield to them, also, it will receive bitfiled of other peers. Store these bitfields in the ArrayList
	for the pieces which value in the bitfield is 0, calculate peer numbers that has this piece, then based on rarefirst, send interest to the peer that has this piece.
	when a peer recieve interest, random unchock peer, sends unchock to the peer which is randomly unchocked
	unchocked peer sends request to the peer, peer responds with piece
	after peer receive piece, it updates its bitfield, then sends have message to all peers which are in ArrayList.
4. Command Introduction

   metainfo: 
   ask the client to show the information about the metainfo file.
   
   announce:
   ask the client to send a GET request to the tracker. Update the information to be used later.
   prints out the status line of the response
   
   trackerinfo:
   prints the information contained in the latest tracker response
   
   show:
   prints the status of all current peer connections
   
   status:
   prints the status of the current downloaded
   
   help:
   prints all the command and its introduction
   
   quit:
   quit the peer cliend
   
 5. Acknowledge
 
    1. We use the library called TrackerInfo, TrackerReader, Utils and ToolKit provided by @author Robert Moore II and @author Deepak, Mike, Josh
       Thanks to their help, we can parse the infohash and peerid and send that to the remote tracker successfully.
       
    2. We use the library called Bencode provided by Wolfgang Ginolas.
       Thanks to his help, we can parse bencoded response successfully.
       
    3. Thanks for Kailash Joshi, who gives us ideas about how to parse the 6byte peer infomation from the tracker response.
  

