import time
import SimpleHTTPServer
import SocketServer
import helper

HOST_NAME = '127.0.0.1'
PORT_NUMBER = 6446
h = helper.Helper()

class MyHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):
    def do_HEAD(s):
        s.send_response(200)
        s.end_headers()
    def do_GET(s):
        responseCode = 200
        response = h.doCommand(s.path)
        s.send_response(responseCode)
        s.end_headers()
        s.wfile.write(response)
    def version_string(s):
        return "Minecraft & Openstack"

if __name__ == '__main__':
    f = open("mc2os_config.txt", "r")
    for line in f:
        line = line.replace("\n","")
        if line.startswith("LISTEN"):
            name = line.split('=')[1]
            HOST_NAME = name.split(':')[0]
            PORT_NUMBER = int(name.split(':')[1])
        if line.startswith("VERSION"):
            pass#helper.VERSION = line.split('=')[1]
        if line.startswith("USERNAME"):
            helper.USERNAME = line.split('=')[1]
        if line.startswith("PASSWORD"):
            helper.PASSWORD = line.split('=')[1]
        if line.startswith("PROJECT"):
            helper.PROJECT = line.split('=')[1]
        if line.startswith("AUTHURL"):
            helper.AUTHURL = line.split('=')[1]
    f.close()
    Handler = MyHandler
    httpd = SocketServer.TCPServer((HOST_NAME, PORT_NUMBER), Handler)

    httpd.serve_forever()
