# -*- coding: utf-8 -*-
from Tkinter import *
import os
from PIL import Image, ImageTk
import exifread
import webbrowser
from googlemaps import Client

class GPSScherm(Frame):
    def __init__(self, master, appVar):
        #Variabelen: Scherm en bijbehorende applicatie
        Frame.__init__(self, master);
        self.grid();
        self.appVar = appVar;
        self.master = master;

        self.gpsAfbeeldingLabelLijst = []; #Array met afbeeldingen om weer te geven.
        self.gpsBestaatNietLabelLijst = []; #Array met labels voor afbeeldingen zonder gps.

        self.afbeeldingI = 0;
        #tijdelijke variable for interfacelementen. De iteratieve variabelen kunnen
        #soms groter zijn dan een aantal van de arrays.

        self.create_widgets();
        self.mainloop();

    def create_widgets(self):
        self.appBoven = Frame(self.master)
        self.appBoven.pack(side = TOP);
        #Maak de bovenkant voor de afbeeldingen en de data.
        self.appOnder = Frame(self.master);
        self.appOnder.pack( side = BOTTOM, fill = BOTH);
        #Maak de onderkant voor de afsluitknop.

        for i in range(3):
            for case in S.witch(self.appVar.afbeeldingPathLijst[i]):
                self.gpsAfbeeldingLabel = Label(self.appBoven, relief = RAISED, bd = 1);
                if case("Leeg"):
                    break;
                    #Doe niets als er geen afbeelding is.
                if case():
                    self.gpsAfbeeldingLabel["image"] = self.appVar.afbeeldingLijst[self.afbeeldingI];
                    self.afbeeldingI += 1;
                    self.gpsAfbeeldingLabel["bg"] = self.appVar.randKleurLijst[i];           
                    self.gpsAfbeeldingLabelLijst.append(self.gpsAfbeeldingLabel);
                    self.gpsAfbeeldingLabelLijst[self.afbeeldingI-1].grid(row = 0, column = self.afbeeldingI);
                    #als er inderdaad een afbeelding is, voeg die toe aan de
                    #array, en tel zodat die niet out-of-bounds gaat.
                    if self.gps_bestaat(self.appVar.afbeeldingPathLijst[i]) == True:
                        self.create_gps_info(self.appVar.afbeeldingPathLijst[i], self.afbeeldingI);
                    else:
                        self.gpsBestaatNietLabel = Label(self.appBoven, text = "GPS onbekend",\
                        relief = RAISED, bd = 1, bg = self.appVar.randKleurLijst[i], fg = "white");
                        self.gpsBestaatNietLabelLijst.append(self.gpsBestaatNietLabel);          
                        self.gpsBestaatNietLabelLijst[-1].grid(row = 1, column = self.afbeeldingI);
                    #Als de gps data aanwezig is, maak dan de bijbehorende items zoals
                    #beschreven in de bijbehorende methode.
                    #Maak anders een label dat aangeeft dat de data niet te vinden is.
        self.afsluitKnop = Button (self.appOnder, height = 4, bd = 4,\
        text = "OK", command = lambda : self.master.destroy());
        self.afsluitKnop.pack (side = BOTTOM, fill = X, expand = True);
        #Maak de knop die het scherm moet afsluiten. Lambda zorgt er wederom voor
        #dat dit niet meteen gebeurt.

    def create_gps_info(self, bestand, positie):      
        f = open(bestand, 'rb'); #Open een bestand.
        tags = exifread.process_file(f); #Laat exifread het bestand lezen.

        latitude = self.verwerk_tag_value(tags['GPS GPSLatitude']);
        latitude = float(latitude[0]+(latitude[1]/60)+((1/float(3600))*(latitude[2]/latitude[3])));
        longitude = self.verwerk_tag_value(tags['GPS GPSLongitude']);
        longitude = float(longitude[0]+(longitude[1]/60)+((1/float(3600))*(longitude[2]/longitude[3])));
        #Zet de inhoud van de tag om van een instance (waar ik niets uit kan halen)
        #naar een gewone array. Reken daarna de graden, minuten en breuk die in
        #de exif staan om in bruikbare coordinaten.
        #graden+(minuut/60)+((1/3600)*(breuk1/breuk2))

        locatieDict = gmaps.reverse_geocode((latitude, longitude))[0][u'address_components'];
        locatieDictLijst = [];
        #reverse geocode de coordinaten en pak de adresonderdelen eruit.
        
        locatieDictLijst.append(str(locatieDict[1][u'long_name'])+" "+str(locatieDict[0][u'long_name']));
        locatieDictLijst.append(str(locatieDict[6][u'long_name']));
        locatieDictLijst.append(str(locatieDict[2][u'long_name']));
        locatieDictLijst.append(str(locatieDict[4][u'long_name']));
        locatieDictLijst.append(str(locatieDict[5][u'long_name']));
        #Straatnaam+Nummer ([1]+[0])
        #Postcode ([6])
        #Plaatsnaam ([2]) ([3] is om de een of andere reden ook plaatsnaam, althans in Nederland)
        #Provincie ([4])
        #Land ([5])
        #Voeg deze dingen in de logische volgorde toe aan een speciaal daarvoor bestemde lijst.
        
        self.mapsKnop = Button(self.appBoven, text = "Maps", command = lambda : self.open_in_maps(latitude, longitude));
        self.mapsKnop.grid(row = 2, column = positie);

        self.coordinatenListBox = Listbox(self.appBoven, height = 2);
        self.coordinatenListBox.insert(1, str(latitude)+str(tags['GPS GPSLatitudeRef']));
        self.coordinatenListBox.insert(2, str(longitude)+str(tags['GPS GPSLongitudeRef']));
        self.coordinatenListBox.grid(row = 1, column = positie);
        #Maak de listbox voor de coordinaten en voeg de latitude/breedtegraad en
        #de longitude/lengtegraad toe aan de list.

        self.locatieListBox = Listbox(self.appBoven, height = 5);
        self.locatieListBox.grid(row = 3, column = positie);
        for i in range(len(locatieDictLijst)):
            self.locatieListBox.insert(i+1, locatieDictLijst[i]);
        #Maak de listbox voor de bijbehorende locatie en voeg alle componenten
        #een voor een toe aan de lijst.

    def gps_bestaat(self, bestand):
        f = open(bestand, 'rb');
        itag = 0;
        tags = exifread.process_file(f);
        for tag in tags.keys():
            if tag in ('GPS GPSLatitude', 'GPS GPSLongitude', 'GPS GPSLatitudeRef', 'GPS GPSLongitudeRef'):
                itag += 1;
        return bool(itag == 4);
        #Checkt of de GPS informatie in de afbeelding zit. Zitten die vier tags
        #erin, return true.

    def verwerk_tag_value(self, tag):
        tagString = str(tag).translate(None, '[]');
        tagString = tagString.replace('/', ', ');
        tagLijst = tagString.split(', ');
        for i in range(4):
            tagLijst[i] = float(tagLijst[i]);
        return tagLijst;
        #Haalt alle onnodige tekens uit de lijst, split de resten en zet deze
        #om in floats voordat de bruikbare lijst gereturnd wordt.
        #[52, 9, 12501/2500] wordt zo 52 9 12501 2500
        #De breuk wordt opgesplitst omdat Python dit anders niet in een float kan omzetten.
        
    def open_in_maps(self, lat, lng):
            deUrl = "https://maps.google.com/maps?q="+str(lat)+","+str(lng);
            #standaardurl ("https://maps.google.com/maps?q="+hele zwik)
            webbrowser.open_new_tab(deUrl);
            
    def exiftag(self, lijst, waarde):
        for tag in lijst.keys():
            if tag in (waarde):
                print tag;
                print type(tag);
                print type(lijst[tag]);
                return lijst[tag];
                
S = __import__("Switch");
gmaps = Client(key='AIzaSyDK7siJT4eG6Oj3Ii_0HCxZQ13oYAzXBpc');
#Import Switch voor gebruik als S.witch, en maak de client. Zonder key kan ik maps
#niet benutten.

if __name__ == "__main__" : GPSScherm.mainloop();