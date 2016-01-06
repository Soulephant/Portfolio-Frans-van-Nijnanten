# -*- coding: utf-8 -*-
from Tkinter import *
from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
import olefile
import os
import os.path
import shutil
import mimetypes
from PIL import Image, ImageTk

################################################################################
#NOOT:
#Officebestanden worden doorgaans gekenmerkt door gebruik van OLE of OLE2
#structuur. Olefile is gemaakt om dit uit te lezen.

class Application(Frame):

    def __init__(self, master):
        Frame.__init__(self,master);
        self.grid();
        
        self.oudDir = "C:/"; #Vorige directory voor het geval er iets niet klopt.
        self.kopieerDir = ""; #Directory om het plakken goed te laten gaan.
        
        self.settingsDict = dict(StartDir="C:", Bogus="released");
        self.settingsDefault = dict(StartDir="C:", Bogus="released");
        #Maak de twee dictionaries voor de instellingen en de default instellingen.
        #Als de settings nog niet bestaan, moeten ze worden aangemaakt met de default instellingen.
        #StartDir = de directory waar de applicatie begint
        #Bogus = een testinstelling puur om meer instellingen te hebben.
        
        settingsAanwezig = False; #De variabele die onthoudt of de settings al aanwezig zijn.
        self.position = 0; #De positie voor het lezen van bestanden.   
        
        self.clipBoardArray = []; #Clipboard van de applicatie
        self.afbeeldingLijst = []; #array met te lezen afbeeldingen.
        self.afbeeldingPathLijst = []; #array met paden van te lezen afbeeldingen.
        
        self.create_widgets();
        
        self.directoryLijst = os.listdir(workingDir);
        #De directorylijst wordt een lijst met directories en bestanden.
        for allesDirectory, itemDirectory in enumerate(self.directoryLijst):
            if itemDirectory == "settings.txt":
                settingsAanwezig = True;
            #Check of settings aanwezig is in de lijst. 
        self.begin_settings(settingsAanwezig);
        #Initialiseer de settings.                

    def create_widgets(self):
        app = Frame(root);
        app.grid();
        
        self.appBoven = Frame(root)
        self.appBoven.pack(side = TOP);
        #Maak de bovenkant voor de navigatiebalk en knoppen.
        self.appMidden = Frame(root);
        self.appMidden.pack(fill = BOTH);
        #Maak het midden voor de lijsten en knoppen.
        self.appImage = Frame(self.appMidden);
        self.appImage.pack( side = RIGHT, fill = Y);
        #Maak een subset voor het exif deel.
        self.appOnder = Frame(root);
        self.appOnder.pack( side = BOTTOM, fill = BOTH);
        #Maak de onderkant voor de externe console.
        self.consoleBoven = Frame(self.appOnder);
        self.consoleBoven.pack(side = TOP, fill = X);
        #Maak de bovenkant van de externe console voor knoppen.
        
        self.menuBar = Menu(root); #Maak het menu.
        self.bestandMenu = Menu(self.menuBar, tearoff = 0); #Maak het bestanddeel van het menu
        self.bestandMenu.add_command(label="Kopiëren", command=self.kopieer);
        self.bestandMenu.add_command(label="Plakken", command=self.plakken);
        self.bestandMenu.add_separator();
        self.bestandMenu.add_command(label="Afsluiten", command=root.destroy);
        #Voeg de items aan het bestanddeel van het menu toe.Omdat de kopieer en plak-
        #methoden afhankelijk zijn van variabelen die bij het opstarten nog niet gezet
        #kunnen zijn, is lambda niet nodig.
        self.menuBar.add_cascade(label="Bestand", menu=self.bestandMenu); #Voeg het bestanddeel aan het menu toe.
        self.metaMenu = Menu(self.menuBar, tearoff = 0); #Maak het bestanddeel van het menu
        self.metaMenu.add_command(label="Metadata kopiëren", command=self.meta_kopieer);
        self.menuBar.add_cascade(label="Metadata", menu=self.metaMenu); #Voeg het bestanddeel aan het menu toe.
        root.config(menu=self.menuBar); #Geef het scherm het menu.
        
        self.directoryBar = Entry(self.appBoven, width = 66);
        self.directoryBar.grid(row = 0, column = 1);
        self.terugKnop = Button(self.appBoven, text = "Terug", command = lambda : self.update_directory(self.directory_terug()));
        self.terugKnop.grid(row = 0, column = 0, sticky = NW);
        self.bevestigKnop = Button(self.appBoven, text = "Ga", command = lambda : self.update_directory(self.directoryBar.get()));
        self.bevestigKnop.grid(row =  0, column = 2);
        #Maak de bar
        
        self.bestandBar = Scrollbar(self.appMidden);      
        self.bestandList = Listbox(self.appMidden, selectmode = EXTENDED, yscrollcommand = self.bestandBar.set, width = 42);
        self.bestandList.pack(side = LEFT, fill = BOTH);
        self.bestandList.bind('<Double-1>', self.dubbel_klik);
        self.bestandBar.pack(side = LEFT, fill = Y);  
        self.bestandBar.configure( command = self.bestandList.yview );
        #Maak de scrollbar en de lijst voor de bestanden. Scrollbars kunnen pas
        #een command krijgen als het betreffende object ook bestaat.
        #Bind zorgt ervoor dat de lijst reageert op de dubbelklik.

        self.imageList = Listbox(self.appImage, height = 3);
        self.imageList.pack();
        self.plaatsKnop = Button(self.appImage, text = "Plaatsten", command = self.update_exif_list);
        self.plaatsKnop.pack(fill = X);
        self.locatieKnop = Button(self.appImage, text = "Locatie", command = self.locatie_exif_list);
        self.locatieKnop.pack(fill = X);
        self.verwijderKnop = Button(self.appImage, text = "Verwijderen", command = self.verwijder_exif_list);
        self.verwijderKnop.pack(fill = X);
        self.exifStartKnop = Button(self.appImage, text = "Start Exif scherm", bg = "#C44747", fg = "white",\
        activebackground = "#C44747", activeforeground = "white", command = self.exif_scherm);
        self.exifStartKnop.pack(fill = X);
        #Maak de lijst en knoppen voor het exif deel. Omdat er maar drie afbeeldingen
        #in de lijst passen is een scrollbar niet nodig. De opstartknop voor het exif
        #scherm is ingewikkelder. Deze is rood (#C44747) wanneer je het scherm niet kan
        #opstarten en groen (#67C447) als dat wel kan. Omdat er niets in de lijst zit
        #begint de knop dus rood.        

        self.metaBar = Scrollbar(self.appMidden);
        self.metaBar.pack(side = RIGHT, fill = Y);
        self.metaList = Listbox(self.appMidden, yscrollcommand = self.metaBar.set);
        self.metaBar.configure( command = self.metaList.yview );
        #Maak de lijst voor metadata met scrollbar.
        
        self.consoleBar = Scrollbar(self.appOnder);
        self.consoleBar.pack(side = RIGHT, fill = Y);
        self.consoleList = Listbox(self.appOnder, selectmode = EXTENDED, yscrollcommand = self.consoleBar.set);
        self.consoleList.pack(fill = BOTH)
        self.consoleBar.configure( command = self.consoleList.yview );
        #Voeg de externe console toe.
        
        self.consoleLabel = Label(self.consoleBoven, text = "Externe Console");
        self.consoleLabel.pack(side = LEFT);        
        self.clearKnop = Button(self.consoleBoven, text = "Console Legen", command = self.console_clear);
        self.clearKnop.pack(side = RIGHT);
        self.copyKnop = Button(self.consoleBoven, text = "Console Kopiëren", command = self.console_copy);
        self.copyKnop.pack(side = RIGHT);        
        self.console_print("Externe Console toegevoegd!");
        #Voeg de knoppen van de externe console toe, en stop een tekst in de console
        #om aan te geven dat die ook daadwerkelijk gemaakt is.
        
        self.metaList.pack(fill = BOTH);
        #pack de meta_lijst als laatste om ervoor te zorgen dat die binnen de layout past.
        
################################################################################
#METHODEN MET BETREKKING TOT INSTELLINGEN
        
    def begin_settings(self, aanwezig):
        if aanwezig == False:
            self.settings = open("settings.txt", "w");
            for s in self.settingsDefault.items():
                editSetting = s[0]+"="+s[1];
                self.settings.write(editSetting+"\n");
                #s[0] is de key van de dictionary. s[1] is de waarde.
                #Schrijf de string in de setting met newline.
            self.console_print("settings.txt aangemaakt!");
            self.settings = open("settings.txt", "r");
            #Maak de settings als ze er nog niet zijn en schrijf de defaultwaarden erin.
        else:
            self.lees_settings("all");
            #Als ze er wel zijn lees je ze allemaal.
        startDir = self.settingsDict["StartDir"];
        self.update_directory(str(startDir));
        #Lees de directory uit de dictronary en update hem.
        bogus = self.settingsDict["Bogus"];
        self.console_print("Bogus "+bogus);
        #Lees de bogus setting uit de dictionary en print hem in de console.
        self.settings.close();    
        
    def wijzig_settings(self, settings, willClose):
        #settings zijn de settings die worden aangepast. willClose spreekt voor zich.
        self.lees_settings(settings);
        self.settings = open("settings.txt", "w");
        for s in self.settingsDict.items():
            editSetting = s[0]+"="+s[1];
            self.settings.write(editSetting+"\n");
            #s[0] is de key, s[1] is de waarde. Schrijf beide erin.
        if willClose == True:
            self.settings.close();
            
    def lees_settings(self, settings):
        #settings zijn de settings die worden aangepast.
        if settings == "all":
            self.settings = open("settings.txt", "r");
            self.position = 0;
            for i in range(len(self.settingsDict)):
                self.settings.seek(self.position);
                setting = str(self.settings.readline());
                setting = (setting.rpartition("=")[2]).rstrip("\n");
                self.settingsDict[self.settingsDict.keys()[i]] = setting;
                self.position = self.settings.tell();
                #Pak het bestand op waar je gestopt was met lezen.
                #Lees de string, pak alles rechts van de = en verwijder de newline
                #(de newline kan echt de settings ruineren anders)
                #Verander de waarde bij de key in kwestie.
                #Sla de positie weer op, en ga zo maar door.
        else:
            for s in settings.items():
                for i in range(len(settings)):
                    self.settingsDict[s[i]] = settings[s[i]];
                    #Wijzig de settingsdictionary.

################################################################################
#METHODEN MET BETREKKING TOT NAVIGATIE

    def update_directory(self, dirVar):
        self.dirVar = dirVar;
        self.directoryBar.delete(0, END); #leeg de navigatiebalk.
        if not (self.dirVar.endswith("/") and self.dirVar.endswith("")):
            self.dirVar = self.dirVar+'/';
        try:
            self.directoryLijst = os.listdir(self.dirVar);
            self.directoryLijst.sort(); #Sorteer de directories
            self.bestandList.delete(0, self.bestandList.size()); #leeg de listbox
            self.directoryBar.insert(END, self.dirVar); #update de navigatiebalk met de nieuwe directory
            self.oudDir = self.dirVar; #De vorige directory wordt de oude directory.            
            self.wijzig_settings({'StartDir': self.dirVar}, True);
            #Wijzig de settings zodat de map de startmap wordt
        except WindowsError:
            self.console_print("Directory bestaat niet: "+self.dirVar);
            self.dirVar = self.oudDir; #Ga naar de vorige directory
            self.directoryBar.insert(END, self.dirVar); #update de navigatiebalk met de nieuwe directory
            self.bestandList.delete(0, self.bestandList.size()); #leeg de listbox
            self.directoryLijst = os.listdir(self.dirVar);
            self.directoryLijst.sort(); #Sorteer de directories
        self.metaList.delete(0, self.metaList.size());
        for i in range(len(self.directoryLijst)):
            self.bestandList.insert(END, self.directoryLijst[i]);
            #insert de items een-voor-een
        self.meta_tel(); #update ook het overzicht van metadata   
            
    def directory_terug(self):
        directory = str(self.directoryBar.get());
        if not (directory.endswith(":/")):
            directory = directory[0:-1];
            directory = directory.rpartition("/")[0];
            #knip de slash eraf zodat de rpartition werkt
        return directory;
        #ga een directory terug
    
    def kopieer(self):
        selectie = self.bestandList.curselection();
        if len(selectie) != 0:
            self.clipBoardArray = []; #Leeg de clipboard
            self.kopieerDir = self.dirVar; #Zet de kopiëerdirectory
            deIndex = self.index_methode(self.bestandList.get(selectie[0]), self.bestandList);
            #variabele voor het daadwerkelijke begin van de selectie, omdat er geen
            #ingebouwde methode is om te getten vanaf een specifiek begin.
            for i in range(len(selectie)):
                self.clipBoardArray.append(self.bestandList.get(i+deIndex));
                #Append elk ding in de selectie beginnend vanaf het begin van de
                #selectie in plaats van het begin van de lijst.
    
    def plakken(self):
        misluktArray = []; #Array voor bestanden die niet geplakt zijn.
        if len(self.clipBoardArray) != 0:
            for i in range(len(self.clipBoardArray)):
                try:
                    shutil.copy2(self.kopieerDir+self.clipBoardArray[i], self.dirVar);
                    self.console_print("Bestand geplakt: "+self.clipBoardArray[i]);
                    #Kopiëer het bestand van de map waar die in zit (kopieerDir)
                    #naar de huidige map.
                except (IOError, WindowsError):
                    misluktArray.append(self.clipBoardArray[i]);
                    self.console_print("Kan bestand niet plakken: "+self.clipBoardArray[i]);
                    #Als dit niet lukt, stop het bestand in de mislukt-array.
            self.update_directory(self.dirVar);

################################################################################
#METHODEN MET BETREKKING TOT METADATA     
                                    
    def meta_lijst(self):
        self.typeLijst = [];
        for allesDirectory, itemDirectory in enumerate(self.directoryLijst):
            #Ga de huidige directory af
            itemTemp = str(mimetypes.guess_type(itemDirectory, strict=False));
            #Sla de metadata even op als variabele
            gelijk = 0;
            for allesMeta, itemMeta in enumerate(self.typeLijst):
                if (itemTemp == itemMeta) or (itemTemp == '(None, None)'):
                    gelijk = 1;
                    #Als de metadatatype al in de lijst zat, wordt die niet nogmaals toegevoegd.
            if gelijk == 0:
                self.typeLijst.append(itemTemp);
                #Gooi de metadata erbij
        
    def meta_tel(self):
        self.meta_lijst();
        #uitvoeren voor het geval dit nog niet gebeurd is.
        for allesMeta, itemMeta in enumerate(self.typeLijst):
            metaTal = 0;
            metaTemp = str(itemMeta);
            if metaTemp == '(None, None)':
                metaTemp = 'Onbekend';
                #Aan 2x none hebben we niks. Bovendien betekent het dat het bestandstype
                #niet herkend wordt, dus geven we dat aan.
            else:
                metaTemp = metaTemp[2:-8];
                #Leestekens eraf en none eraf
            for allesDirectory, itemDirectory in enumerate(self.directoryLijst):
                #Ga de huidige directory af
                itemTemp = str(mimetypes.guess_type(itemDirectory, strict=True));
                #Sla de metadata even op als variabele
                if itemTemp == itemMeta:
                    metaTal += 1;
                    #teller met 1 omhoog als het inderdaad het betreffende
                    #bestandstype is.
            self.metaList.insert(END, metaTemp+": "+str(metaTal)+" items");
            #Voeg het metadataype toe aan de lijst en het aantal items dat het heeft.
            
    def meta_kopieer(self):
        self.console_print("Begonnen met kopiëren van metadata");
        try:
            os.mkdir(self.dirVar+"meta/");
            self.console_print("Directory gemaakt: "+self.dirVar+"meta/");
        except OSError:
            self.console_print("Kan directory niet maken: "+self.dirVar+"meta/");
        #Probeer een map te maken voor alle gekopiëerde bestanden.
        for allesMeta, itemMeta in enumerate(self.typeLijst):
            metaTemp = str(itemMeta); #Sla de metadata op zodat je die aan kan passen.
            if metaTemp == '(None, None)':
                metaTemp = 'Onbekend';
                #Aan 2x none hebben we niks. Bovendien betekent het dat het bestandstype
                #niet herkend wordt, dus geven we dat aan.
            else:
                metaTemp = metaTemp[2:-8];
                #Leestekens eraf en none eraf
            try:
                os.makedirs(self.dirVar+"meta/"+metaTemp);
                self.console_print("Directory gemaakt: "+self.dirVar+"meta/"+metaTemp);
                #Probeer de directory te maken voor alles met dezelfde extensie
            except OSError:
                self.console_print("Kan directory niet maken: "+self.dirVar+"meta/"+metaTemp);
            for allesDirectory, itemDirectory in enumerate(self.directoryLijst):     
                itemTemp = str(mimetypes.guess_type(itemDirectory, strict=True));
                #Sla de metadata op als variabele
                if itemTemp == itemMeta:
                    try:
                        shutil.copy2(self.dirVar+itemDirectory, self.dirVar+"meta/"+metaTemp);
                        self.console_print("Bestand geplakt: "+itemDirectory);
                        #Probeer het item in de directory te kopiëren
                        #naar de map van de extensie
                    except IOError:
                        break;
                        #anders niet
            self.update_directory(self.dirVar);

################################################################################
#METHODEN MET BETREKKING TOT HET EXIF GEDEELTE         
                                    
    def update_exif_list(self):
        selectie = self.bestandList.curselection();
        if len(selectie) != 0:
            if len(selectie) <= 3-self.imageList.size():
                deIndex = self.index_methode(self.bestandList.get(selectie[0]), self.bestandList);
                #variabele voor het daadwerkelijke begin van de selectie
                for i in range(len(selectie)):
                    self.file = self.bestandList.get(i+deIndex); #Pak de bestandsnaam
                    try:
                        os.chdir(self.dirVar); #Maak de huidige directory de working directory om het lezen te laten werken.
                        self.file = Image.open(self.file); #Leest het bestand.
                        self.afbeeldingLijst.append(ImageTk.PhotoImage\
                        (self.file.resize(self.bereken_ratio(self.file, 128), Image.ANTIALIAS)));                      
                        self.afbeeldingPathLijst.append(self.dirVar+"/"+self.bestandList.get(i+deIndex));
                        self.imageList.insert(END, self.bestandList.get(i+deIndex));
                        #Maak een PhotoImage van de file zodat die kan worden
                        #weergegeven. Voeg vervolgens de file toe aan de array
                        #voor bestanden, en het pad aan de array voor paden.
                        
                        #Als deze handelingen mogelijk zijn wordt de filepath
                        #toegevoegd aan de lijst.
                    except (IOError, WindowsError):
                        self.console_print(self.file+" is geen geldige afbeelding.");
                        #Anders wordt de path niet toegevoegd.            
            else:
                self.console_print("De selectie is te groot. Er passen maximaal drie afbeeldingen.");
                #Als er te veel afbeeldingen toegevoegd zouden worden met de huidige selectie
                #wordt er niets toegevoegd.
        if self.imageList.size() > 0:
            self.exifStartKnop.configure(bg = "#67C447", activebackground = "#67C447");
            #Als er nu afbeeldingen in de lijst zitten, kan het exifscherm worden opgestart
            #en wordt de knop dus groen.
            
    def verwijder_exif_list(self):
        selectie = self.imageList.curselection();
        if len(selectie) != 0:
            deIndex = self.index_methode(self.imageList.get(selectie[0]), self.imageList);
            #variabele voor het daadwerkelijke begin van de selectie
            self.imageList.delete(deIndex);
            self.afbeeldingLijst.remove(self.afbeeldingLijst[deIndex]);
            self.afbeeldingPathLijst.remove(self.afbeeldingPathLijst[deIndex]);
            #Verwijder de afbeelding uit de lijst, en de afbeelding- en patharrays.
        if self.imageList.size() == 0:
            self.exifStartKnop.configure(bg = "#C44747", activebackground = "#C44747");
            #Als er geen afbeeldingen in de lijst zitten, kan het exifscherm niet meer
            #worden opgestart en wordt de knop dus rood.        
    
    def locatie_exif_list(self):
        selectie = self.imageList.curselection();
        if len(selectie) != 0:
            deIndex = self.index_methode(self.imageList.get(selectie[0]), self.imageList);
            self.console_print(self.afbeeldingPathLijst[deIndex]);
            #print de locatie van de afbeelding voor als je het overzicht kwijtraakt.
            
    def exif_scherm(self):
        os.chdir(workingDir);
        module = __import__("ExifComponent"); #importeer het exif scherm
        window = Toplevel(); #Maak toplevel scherm voor het exif deel
        window.geometry("640x640");
        window.title("Exif scherm");
        dit = module.ExifScherm(window, self);

################################################################################
#METHODEN MET BETREKKING TOT DE CONSOLE
                                                    
    def console_copy(self):
        selectie = self.consoleList.curselection();
        if len(selectie) != 0:
            root.clipboard_clear();
            deIndex = self.index_methode(self.consoleList.get(selectie[0]), self.consoleList);
            #variabele voor het daadwerkelijke begin van de selectie
            for i in range(len(selectie)):
                root.clipboard_append(self.consoleList.get(i+deIndex)+"\n");
                #Append elk ding in de selectie beginnend vanaf het begin van de
                #selectie in plaats van het begin van de lijst.
        else:
            for i in range(self.consoleList.size()):
                root.clipboard_append(self.consoleList.get(i)+"\n");
        
    def console_clear(self):
        self.consoleList.delete(0, self.consoleList.size());
        #Leeg de console. Dit is een aparte methode omdat de grootte van de list
        #kan verschillen.
    
    def console_print(self, line):
        self.consoleList.insert(END, line);
        self.consoleList.yview_moveto(1);
        #Insert alleen had ik geen methode voor gebruikt, maar de list moet ook
        #automatisch naar beneden scrollen, en daar is yview_moveto voor. Tussen
        #haakjes staat de fractie, in dit geval 1 omdat er naar het einde van de
        #lijst gescrolld moet worden.

################################################################################
#ANDERE METHODEN

    def dubbel_klik(self, event):
        #methode voor het lezen van bestanden of navigeren van directories via de bestandList
        selectie = str(self.bestandList.get(self.bestandList.curselection()));
        fp = self.dirVar+selectie; #Pak de filepath omdat deze nog wel vaker gebruikt wordt.
        #pak de selectie zonder extensies
        #rpartition returnt "" als het scheidingsteken niet gevonden kan worden
        if selectie.rpartition(".")[0] == "":
            self.update_directory(self.dirVar+selectie.rpartition(".")[2]);
            #update de directory als er geen extensie is en het dus een map is.
        else:
            #anders kijken of het bestand een pdf of office bestand is
            if selectie.rpartition(".")[2] == "pdf":
                try: 
                    file = open(fp, 'rb');
                    parser = PDFParser(file);
                    #open het bestand en een parser
                    doc = PDFDocument(parser, None);
                    docDict = doc.info[0];
                    #parse het document. Om een of andere reden is doc.info een
                    #lijst met een dictionary als enig item. Haal de dictionary eruit.
                    self.console_print("\""+selectie+"\" is een pdf-bestand.");
                    for tag in docDict.keys():
                        if docDict[tag] != "":
                            self.console_print("%s: %s" % (tag, docDict[tag]));
                        #print alle tags die niet leeg zijn. %s en %s zijn 
                        #respectievelijk de eerste en tweede waarden ernaast.
                    file.close();
                except (IOError, WindowsError):
                    self.console_print("\""+selectie+"\" kan niet gelezen worden.");            
            #Noot: Olefile leest geen nieuwe office extensies zoals docx.
            if olefile.isOleFile(fp) == True:
                try:
                    ole = olefile.OleFileIO(fp);
                    meta = ole.get_metadata();
                    #open het bestand in read-only modus, en lees de metadata
                    if ole.exists('WordDocument'):
                        self.console_print("\""+selectie+"\" is een wordbestand.");
                    if ole.exists('PowerPoint Document'):
                        self.console_print("\""+selectie+"\" is een powerpointbestand.");
                    if ole.exists('Workbook'):
                        self.console_print("\""+selectie+"\" is een excelbestand.");
                    #Kijk of er in de storages aangegeven wordt dat het bestand een
                    #word, powerpoint danwel excelbestand is.
                    metaDict = dict(Auteur=str(meta.author), Titel=str(meta.title),\
                    Creatiedatum=str(meta.create_time));
                    #Alleen de creatiedatum, auteur en titel zijn van belang. Maak er een dictionary mee.
                    for tag in metaDict.keys():
                        if metaDict[tag] == "":
                            metaDict[tag] = "Geen";
                        self.console_print("%s: %s" % (tag, metaDict[tag]));
                        #Als er geen waarde is, is het handiger als er Geen staat.
                    ole.close()
                except (IOError, WindowsError):
                    self.console_print("\""+selectie+"\" kan niet gelezen worden.");
                                                            
    def index_methode(self, bestand, lijst):
        i = lijst.get(0); #Pak het eerste item in de lijst
        echteIndex = 0;
        while i !=  bestand:
            echteIndex += 1;
            i = lijst.get(echteIndex);
            #Ga de lijst af net zolang tot je het bestand gevonden hebt.
        return echteIndex; #return de positie, en dus waar de selectie begint.
    
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

workingDir = "C:/Users/Frans 2.1/Documents/School/IWL2/Python/"; #De directory waar de applicatie doorgaans moet zoeken
S = __import__("Switch");
       
root = Tk();  
root.title("Surrogaat verkenner"); 
root.geometry("800x480");

app = Application(root);

root.mainloop();