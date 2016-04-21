from novaclient import client
from novaclient import exceptions

VERSION = "2.0"
USERNAME = "admin"
PASSWORD = "Passw0rd"
PROJECT = "admin"
AUTHURL = "http://10.250.168.3:5000/v2.0"

def GetNova():
    nova = client.Client(VERSION,
                         USERNAME,
                         PASSWORD,
                         PROJECT,
                         AUTHURL)
    return nova

class Helper:
    def doCommand(this,command):
        if command.startswith("/neutron/list"):
            return this.listNetworks()

        if command.startswith("/flavor/list"):
            return this.listFlavors()

        if command.startswith("/glance/list"):
            return this.listImages()

        if command.startswith("/nova/list"):
            return this.listServers()

        if command.startswith("/nova/create/"):
            params = command[13:]
            return this.createServer(params)

        if command.startswith("/nova/delete/"):
            params = command[13:]
            return this.deleteServer(params)

        if command.startswith("/nova/start/"):
            servername = command[12:]
            return this.startServer(servername)

        if command.startswith("/nova/stop/"):
            servername = command[11:]
            return this.stopServer(servername)
        return "error:Bad request"

    def listNetworks(this):
        nova = GetNova()
        result = ""
        for network in nova.networks.list():
            #print(dir(network))
            result += network.human_id + ";" + network.id + "&"
        return result[:-1]

    def listFlavors(this):
        nova = GetNova()
        result = ""
        for flavor in nova.flavors.list():
            result += flavor.name + ";" + flavor.id + "&"
        return result[:-1]

    def listImages(this):
        nova = GetNova()
        result = ""
        for image in nova.images.list():
            result += image.name + ";" + image.id + "&"
        return result[:-1]

    def listServers(this):
        nova = GetNova()
        result = ""
        for server in nova.servers.list():
            result += server.name + ";" + server.id + ";" + this.getState(server) + "&"
        return result[:-1]

    def createServer(this,params):
        params = params.split("/")
        if len(params) != 4:
            return "error:Bad format"
        name = params[0]
        imageId = params[1]
        imageObject = "error"
        flavorId = params[2]
        flavorObject = "error"
        networks = params[3].split("+")
        nics = []
        nova = GetNova()
        
        for network in nova.networks.list():
            #print (dir(network))
            if network.id in networks:
                nic = {"net-id":network.id}
                nics.append(nic)
        if nics == []:
            return "error:No networks added"

        for server in nova.servers.list():
            if name == server.name:
                return "error:An instance with that name already exists"

        for image in nova.images.list():
            if imageId == image.id:
                imageObject = image
                break
        if imageObject == "error":
            return "error:No such image name"

        for flavor in nova.flavors.list():
            if flavorId == flavor.id:
                flavorObject = flavor
                break
        if flavorObject == "error":
            return "error:No such flavor name"

        try:
            newServer = nova.servers.create(name = name,
                                            image = imageObject,
                                            flavor = flavorObject,
                                            nics = nics)
        except exceptions.BadRequest as e:
            return "error:" + e.message
            
        return newServer.name + ";" + newServer.id + ";" + this.getState(newServer)

    def deleteServer(this,name):
        nova = GetNova()
        serverObject = "error"
        for server in nova.servers.list():
            if name == server.id:
                serverObject = server
                break
        if serverObject == "error":
            return "error:No such instance name"
        if this.getState(serverObject) != "shutoff" and this.getState(serverObject) != "error":
            return "error:Instance still running"
        serverObject.delete()
        return "Deleted " + name 

    def startServer(this,name):
        nova = GetNova()
        serverObject = "error"
        for server in nova.servers.list():
            if name == server.id:
                serverObject = server
                break
        if serverObject == "error":
            return "error:No such instance name"
        if this.getState(serverObject) != "shutoff":
            return "error:Instance already running"
        serverObject.start()
        return "Started " + name

    def stopServer(this,name):
        nova = GetNova()
        serverObject = "error"
        for server in nova.servers.list():
            if name == server.id:
                serverObject = server
                break
        if serverObject == "error":
            return "error:No such instance name"
        if this.getState(serverObject) != "active":
            return "error:Cannot stop instance"
        serverObject.stop()
        return "Stopped " + name

    def getState(this,server):
        state = server.status.lower()
        task = getattr(server,"OS-EXT-STS:task_state")
        if task is not None:
            state = task.lower()
        return state 

