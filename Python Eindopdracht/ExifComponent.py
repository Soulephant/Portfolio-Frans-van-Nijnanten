# -*- coding: utf-8 -*-
from Tkinter import *
import os
import datetime
import exifread

class ExifScherm(Frame):

    def __init__(self, master, appVar):
        Frame.__init__(self,master);
        self.grid();
        self.master = master;
        self.appVar = appVar;
        
        self.done = False; #een variabele om te checken of de invoer klaar is.
        self.afbeeldingKnopLijst = []; #array voor interfaceelementen
        self.afbeeldingI = 0;
        #tijdelijke variable for interfacelementen. De iteratieve variabelen kunnen
        #soms groter zijn dan een aantal van de arrays.
        
        self.afbeeldingLijst = []; #array met te lezen afbeeldingen.
        self.afbeeldingPathLijst = []; #array met paden van te lezen afbeeldingen. Deze wordt alleen gebruikt bij het opstarten.
        
        for i in range(len(appVar.afbeeldingLijst)):
            self.afbeeldingLijst.append(appVar.afbeeldingLijst[i]);  
            self.afbeeldingPathLijst.append(appVar.afbeeldingPathLijst[i]);
            #Voeg de afbeeldingen en paths handmatig aan de lijst toe. Gebeurt dit niet,
            #dan worden afbeeldingen uit dit scherm verwijderd als ze van de lijst verwijderen
            #in de hoofdapplicatie

        self.afbeeldingDatumLijst = [];
        self.afbeeldingTijdLijst = []; #array met creatie- en wijzigingsdata om de vroegste en recentste makkelijker te vinden.
        self.randKleurLijst= ["#8A0000","#058A00","#00408A"]; #array voor kleuren: Rood, Groen, Blauw
        
        for i in range(3-len(self.afbeeldingPathLijst)):
            self.afbeeldingPathLijst.insert(-1, "Leeg");
            #Vul de rest van de lijst aan met leeg.
            
        self.create_widgets();
        self.mainloop();
            
    def create_widgets(self):

        appBoven = Frame(self.master)
        appBoven.pack(side = TOP);
        #Maak de bovenkant voor de knoppen en de data.
        appOnder = Frame(self.master);
        appOnder.pack( side = BOTTOM, fill = BOTH);
        #Maak de onderkant voor de tijdlijn.
        appLinks = Frame(appBoven);
        appLinks.pack( side = LEFT, fill = Y);
        #Maak de linkerhelft voor de knoppen
        appRechts = Frame(appBoven);
        appRechts.pack( side = RIGHT, fill = BOTH);
        #Maak de rechterhelft voor de data. Deze moet zoveel mogelijk ruimte
        #krijgen, wat mogelijk wordt gemaakt door fill BOTH.
       
        self.exifScrollbar = Scrollbar(appRechts);
        self.exifScrollbar.pack(side = LEFT, fill = Y);
        self.exifList = Listbox(appRechts, yscrollcommand = self.exifScrollbar.set, width = 64);    
        self.exifList.pack( side = LEFT, fill = BOTH, expand = True );
        self.exifScrollbar.configure( command = self.exifList.yview );
        #Maak de scrollbar en de lijst voor de exif data. Scrollbars kunnen pas
        #een command krijgen als het betreffende object ook bestaat.

        for i in range(3): #maak de drie "afbeeldingen"
            for case in S.witch(self.afbeeldingPathLijst[i]):
                self.afbeeldingKnop = Label(appLinks, relief = RAISED, bd = 1);
                if case("Leeg"):
                    self.afbeeldingKnop.configure(text = "Geen afbeelding", fg = "white");
                    break;
                    #In geval dat er geen afbeelding is of een nietbestaande,
                    #voeg gewoon tekst toe. De fg bepaalt de kleur van de tekst.
                if case():
                    self.afbeeldingKnop = Button(appLinks);
                    self.afbeeldingKnop["image"] = self.afbeeldingLijst[self.afbeeldingI];
                    self.afbeeldingKnop["command"] = lambda: self.update_exif(i+1);
                    self.afbeeldingTijdLijst.append(os.path.getctime(self.afbeeldingPathLijst[i]));
                    self.afbeeldingTijdLijst.append(os.path.getmtime(self.afbeeldingPathLijst[i]));
                    self.afbeeldingI += 1;
                    #als er inderdaad een afbeelding is, voeg die toe aan de
                    #interface array, en tel zodat de afbeeldingenarray niet
                    #out-of-bounds gaat.
                    #Lambda zorgt ervoor dat de functie niet bij het aanmaken
                    #van de knop wordt aangeroepen.
                    #Voeg ook meteen de creatie- en wijzigingsdata toe aan een array
                    #voor verdere raadpleging.
            self.afbeeldingKnop["bg"] = self.randKleurLijst[i];            
            self.afbeeldingKnopLijst.append(self.afbeeldingKnop);
            self.afbeeldingKnopLijst[i].grid(row = i, column = 0, sticky = W);
            #Maak de knop en voeg die toe aan een lijst zodat ik geen id nodig heb.

        try:
            self.afbeeldingKnopLijst[0]["command"] = lambda: self.update_exif(1);
        except TclError:
            pass;
        try:
            self.afbeeldingKnopLijst[1]["command"] = lambda: self.update_exif(2);
        except TclError:
            pass;
        try:
            self.afbeeldingKnopLijst[2]["command"] = lambda: self.update_exif(3);
        except TclError:
            pass;
        #De knoppen krijgen hun commands afzonderlijk, omdat lambda niet werkt in een for-loop.
        #Mijn excuses voor deze onelegante oplossing.

        self.tijdLijnScrollbar = Scrollbar(appOnder, orient = HORIZONTAL);
        self.tijdLijnScrollbar.pack(side = TOP, fill = X);
        self.tijdSchaal = 256*self.afbeeldingI;
        #Maak de scrollbar voor de tijdlijn. Bereken ook de schaal voor de tijdlijn.
        #Hoe meer afbeeldingen, hoe meer ruimte je nodig hebt.
        self.tijdLijnCanvas = Canvas(appOnder, xscrollincrement = 32,\
        xscrollcommand = self.tijdLijnScrollbar.set, width = 128+(self.tijdSchaal),\
        scrollregion= (0, 0, 128+(self.tijdSchaal), 0), bg = "#949494");
        self.tijdLijnCanvas.pack(side = BOTTOM, fill = X);
        self.tijdLijnScrollbar.configure(command = self.tijdLijnCanvas.xview );
        #Maak de canvas. Er moet 64 pixels aan beide weerszijden zitten om de
        #begin- en eindlabels te bevatten. Daarom 128+de tijdschaal.

        self.beginTijd = min(self.afbeeldingTijdLijst);
        self.eindTijd = max(self.afbeeldingTijdLijst);
        #Bereken de vroegste en meest recente tijden. De array maakt dit veel makkelijker.

        for i in range(6): #maak de labels die de data aangeven.
            self.afbeeldingDatum = Message(self.tijdLijnCanvas, bd = 1,\
            bg = self.randKleurLijst[i/2], relief = RAISED, fg = "white", width = 160);
            #De breedte is ingesteld om ervoor te zorgen dat alles op twee regels blijft.
            #i gedeeld door twee geeft vanwege python's deeloperatie altijd een naar beneden
            #afgerond getal. Zo kan ik de afbeeldingen pakken die ik nodig heb en toch tot
            #zes labels maken.
            for case in S.witch(self.afbeeldingPathLijst[i/2]):
                if case("Leeg"):
                    break;
                    #doe niets als er geen afbeelding is.
                if case():
                    if i%2 == 0: #Een even i (of 0) gaat altijd over creatiedata.                    
                        self.xPositie = int(((os.path.getctime(self.afbeeldingPathLijst[i/2])\
                        -self.beginTijd)/(self.eindTijd-self.beginTijd))*self.tijdSchaal);
                        self.afbeeldingDatum["text"] = "Gemaakt: \n"+\
                        str(datetime.datetime.fromtimestamp(os.path.getctime\
                        (self.afbeeldingPathLijst[i/2]))).rpartition(":")[0];
                    else: #Een oneven i gaat altijd over wijzigingsdata.
                        self.xPositie = int(((os.path.getmtime(self.afbeeldingPathLijst[i/2])\
                        -self.beginTijd)/(self.eindTijd-self.beginTijd))*self.tijdSchaal);
                        self.afbeeldingDatum["text"] = "Gewijzigd: \n"+\
                        str(datetime.datetime.fromtimestamp(os.path.getmtime\
                        (self.afbeeldingPathLijst[i/2]))).rpartition(":")[0];
                    #De positie is de creatie- of wijzigingsdatum minus begindatum gedeeld
                    #door de einddatum minus de begindatum,in abstracte waarden. Vermenigvuldig daarna met de tijdschaal.
                    #Stel vervolgens de betreffende tekst in. Verander de abstracte waarde in
                    #een begrijpelijke datum-tijd combinatie. Omdat ik niet geinteresseerd ben
                    #in seconden en microseconden, haalt rpartition alles na de laatste : weg.
                    self.tijdLijnCanvas.create_window((64+self.xPositie, 16+(i*32)), window=self.afbeeldingDatum);
                    #Maak de label. De y-positie is telkens iets lager om alles min of meer
                    #te laten passen.

        self.beginLabel = Label(self.tijdLijnCanvas, bd = 1,relief = RAISED,\
        text = datetime.date.fromtimestamp(self.beginTijd));
        self.eindLabel = Label(self.tijdLijnCanvas, bd = 1,relief = RAISED,\
        text = datetime.date.fromtimestamp(self.eindTijd));
        self.tijdLijnCanvas.create_window((32, 208), window=self.beginLabel);
        self.tijdLijnCanvas.create_window(((self.tijdSchaal)+96, 208), window=self.eindLabel);      
        #Maak de labels voor de vroegste en meest recente tijden.

        self.gpsKnop = Button(appRechts, text = "GPS Info", command = self.gps_scherm);
        self.gpsKnop.pack(side = RIGHT, fill = BOTH);
    
    def update_exif(self, buttonNummer):
        self.exifList.delete (0, self.exifList.size()); #Leeg de lijst.
        f = open(self.afbeeldingPathLijst[buttonNummer-1], 'rb'); #Open een bestand.
        tags = exifread.process_file(f); #Laat exifread het bestand lezen.
        for tag in tags.keys():
            if tag not in ('JPEGThumbnail', 'TIFFThumbnail', 'EXIF MakerNote'):
                self.exifList.insert(END, "%s = %s" % (tag, tags[tag]));
            #Filter de tags zodat alleen de interessante tags worden weergegeven.

    def gps_scherm(self):
        module = __import__("GPSComponent"); #importeer het gps scherm
        window = Toplevel(); #Maak toplevel scherm voor het gps deel
        window.geometry(str(self.afbeeldingI*224)+"x480"); #de breedte hangt af van het aantal afbeeldingen
        window.title("GPS informatie"); 
        dit = module.GPSScherm(window, self);
        #geef deze applicatie mee zodat het gps scherm bij de nodige variabelen kan
                            
    def bereken_ratio(self, bestand, maxDim):
        self.w = float(bestand.size[0]);
        self.h = float(bestand.size[1]);
        #Verander de breedte of hoogte in een float. Anders krijgen we een integer-deling.
        self.ratio = max(self.w/maxDim, self.h/maxDim);
        if self.ratio < 1:
            self.ratio = 1;
        #stel de ratio in op de grootste van de twee waarden gedeeld door het
        #maximum formaat. De ratio moet minimaal 1 zijn. Anders worden de afbeeldingen vergroot.
        self.w /= self.ratio;    
        self.h /= self.ratio;
        return (int(self.w), int(self.h));
             
S = __import__("Switch");
#Omdat dit nog in de tussenstappen map zit, moet ik de working directory
#veranderen om S.witch te gebruiken.

if __name__ == "__main__" : ExifScherm.mainloop();