package com.cloudbase.openstackmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OpenStackConnector implements Runnable{
	private static String server;
	
	private static HashMap<Integer, String> instanceCommandMap;
	private static ArrayList<Integer> instanceCommandToDo;
	
	private static Object glance_mutex;
	private static Object neutron_mutex;
	private static Object nova_mutex;
	private static Object instance_mutex;
	private static Object command_mutex;
	
	private static String glanceImages;
	private static String neutronNICs;
	private static String novaFlavors;
	private static String novaInstances;

	public OpenStackConnector(String MC2OS_server){
		glanceImages = "";
		neutronNICs = "";
		novaFlavors = "";
		novaInstances = "";
		
		server = "http://" + MC2OS_server + "/";
		
		glance_mutex = new Object();
		neutron_mutex = new Object();
		nova_mutex = new Object();
		instance_mutex = new Object();
		command_mutex = new Object();

		instanceCommandMap = new HashMap<Integer, String>();
		instanceCommandToDo = new ArrayList<Integer>();
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				URL url;
				URLConnection conn;
				BufferedReader in;
				String inputLine;
				
				//query glance images
				url = new URL(server + "glance/list");
				conn = url.openConnection();
				in = new BufferedReader(
                        new InputStreamReader(
                        conn.getInputStream()));
				
				inputLine = in.readLine();
				
				in.close();
				
				updateGlanceImages(inputLine);
				
				
				//query neutron networks
				url = new URL(server + "neutron/list");
				conn = url.openConnection();
				in = new BufferedReader(
                        new InputStreamReader(
                        conn.getInputStream()));
				
				inputLine = in.readLine();
				
				in.close();
				
				updateNeutronNICs(inputLine);
				
				
				//query neutron networks
				url = new URL(server + "flavor/list");
				conn = url.openConnection();
				in = new BufferedReader(
                        new InputStreamReader(
                        conn.getInputStream()));
				
				inputLine = in.readLine();
				
				in.close();
				
				updateNovaFlavors(inputLine);
				
				
				//query instances status
				url = new URL(server + "nova/list");
				conn = url.openConnection();
				in = new BufferedReader(
                        new InputStreamReader(
                        conn.getInputStream()));
				
				inputLine = in.readLine();
				
				in.close();
				
				updateNovaInstances(inputLine);
				
				
				////////////////////////////////////////////////////////////////////////////////
				//treat all the instance creation requests
				synchronized(command_mutex){
					for(int i=0;i<instanceCommandToDo.size();i++){
						int key = instanceCommandToDo.get(i);
						if(instanceCommandMap.get(key).startsWith("TODO:")){
							String oldData = instanceCommandMap.get(key).substring(5);
							instanceCommandMap.put(key, "DONE:" + OpenStackConnector.issueCreateInstance(oldData));
						}
						
						instanceCommandToDo.remove(i);
						i--;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
		}
	}
	
	public void updateGlanceImages(String str){
		synchronized(glance_mutex){
			glanceImages = str;
		}
	}
	
	public void updateNeutronNICs(String str){
		synchronized(neutron_mutex){
			neutronNICs = str;
		}
	}
	
	public void updateNovaFlavors(String str){
		synchronized(nova_mutex){
			novaFlavors = str;
		}
	}
	
	public void updateNovaInstances(String str){
		synchronized(instance_mutex){
			novaInstances = str;
		}
	}
	
	public static String getGlanceImages(){
		String ret;
		synchronized(glance_mutex){
			ret = glanceImages;
		}
		return ret;
	}

	public static String getNeutronNICs() {
		String ret;
		synchronized(neutron_mutex){
			ret = neutronNICs;
		}
		return ret;
	}
	
	public static String getNovaFlavors(){
		String ret;
		synchronized(nova_mutex){
			ret = novaFlavors;
		}
		return ret;
	}
	
	public static String getNovaInstances(){
		String ret;
		synchronized(instance_mutex){
			ret = novaInstances;
		}
		return ret;
	}
	
	public static int createInstance(String params){
		int key = new Random().nextInt();
		synchronized(command_mutex){
			while(instanceCommandMap.containsKey(key))
				key = new Random().nextInt();
			
			instanceCommandMap.put(key, "TODO:" + params);
			instanceCommandToDo.add(key);
		}
		return key;
	}
	
	public static String getRequestResponse(int key){
		String ret;
		synchronized(command_mutex){
			ret = instanceCommandMap.get(key);
		}
		return ret;
	}
	
	private static String issueCreateInstance(String params){
		String inputLine = "";
		String randomName = "Minecraft_";
		for(int i=0;i<20;i++)
			randomName += Character.toString((char)(new Random().nextInt('z'-'a')+'a'));
		URL url;
		try {
			String request = "nova/create/" + randomName + "/" + params;
			url = new URL(server + request);
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(
	                new InputStreamReader(
	                conn.getInputStream()));
			
			inputLine = in.readLine();
			
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inputLine;
	}

	public static void startInstance(String instanceID) {
		URL url;
		try {
			String request = "nova/start/" + instanceID;
			url = new URL(server + request);
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(
	                new InputStreamReader(
	                conn.getInputStream()));
			
			String inputLine = in.readLine();
			
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void stopInstance(String instanceID) {
		URL url;
		try {
			String request = "nova/stop/" + instanceID;
			url = new URL(server + request);
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(
	                new InputStreamReader(
	                conn.getInputStream()));
			
			String inputLine = in.readLine();
			
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void terminateInstance(String instanceID) {
		URL url;
		try {
			String request = "nova/delete/" + instanceID;
			url = new URL(server + request);
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(
	                new InputStreamReader(
	                conn.getInputStream()));
			
			String inputLine = in.readLine();
			
			in.close();
			
			System.out.println(inputLine);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
