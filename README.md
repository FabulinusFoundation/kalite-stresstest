#KA-Lite Stress Test
KA-Lite Stress Test is a tool to simulate clients accessing a KA-Lite server (https://github.com/learningequality/ka-lite). It is intended as a tool to test single board computers as the Raspberry Pi that are running KA-Lite to check whether they are capable to handle a certain amount of clients.

With KA-Lite Stress Test you can set the IP address, the port, and the number of clients to simulate. It logs what is going on to the user interface, e.g. if a connection times out, etc.

Each client runs in its own thread. Currently, this tool supports only accessing a page, simulation of video streaming is coming soon.
