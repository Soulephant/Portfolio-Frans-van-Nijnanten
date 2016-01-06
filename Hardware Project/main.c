/*
 * Copyright 2012 Alan Burlison, alan@bleaklow.com.  All rights reserved.
 * Use is subject to license terms.
 */

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#include <math.h>       /* floor */

#include "WS2811.h"

#define BIT(B)           (0x01 << (uint8_t)(B))
#define SET_BIT_HI(V, B) (V) |= (uint8_t)BIT(B)
#define SET_BIT_LO(V, B) (V) &= (uint8_t)~BIT(B)

void ProcessPressedButton(int ButtonPressed); //Alles wat er gebeurt als de knop(pen) word(en) ingedrukt
int Pressed_Confidence_Level[2]; //Houdt bij hoeveel milliseconden de knop is ingedrukt
int Released_Confidence_Level[2]; //Houdt bij hoeveel milliseconden de knop is losgelaten
int teller = 0; //Welk patroon?
//0 = uit
//1 = veld-zig-zag
//2 = random kleuren
int knopVrij = 1;
int muziekKnopVrij = 1;
//0 = Knop kan niet worden ingedrukt tot deze wordt losgelaten
//1 = Knop kan worden ingedrukt

int i = 0; //i is i <_<
int muziek = 0;
//0 = niet-muziek modus
//1 = muziek modus

int sterkte = 0; //Sterkte variabele
int stappen = 0; //De algemene variabele voor stappen in een patroon
int extraTeller = 0;
int discoKleur = 1;
int ADCWaarde = 0;

//Variabelen die het rekenen met patronen een stuk makkelijker maakt
int sectieLEDS;
int stappenLEDS;

RGB_t rgb[72] =
{
		{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},
		{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},
		{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},
		{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},
		{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},
		{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0}
};
//RGB array

int matrixArray[12][6] =
{
	{0,1,2,3,4,5},
	{11,10,9,8,7,6},
	{12,13,14,15,16,17},
	{23,22,21,20,19,18},
	{24,25,26,27,28,29},
	{35,34,33,32,31,30},
	{36,37,38,39,40,41},
	{47,46,45,44,43,42},
	{48,49,50,51,52,53},
	{59,58,57,56,55,54},
	{60,61,62,63,64,65},
	{71,70,69,68,67,66}
};
//Matrix die rekenen met de leds makkelijker maakt

int discoArray[6][6] =
{
	{1,6,1,6,1,6},
	{2,5,2,5,2,5},
	{3,4,3,4,3,4},
	{4,3,4,3,4,3},
	{5,2,5,2,5,2},
	{6,1,6,1,6,1}
};
int discoArrayVorige[6][6] =
{
	{6,1,6,1,6,1},
	{1,6,1,6,1,6},
	{2,5,2,5,2,5},
	{3,4,3,4,3,4},
	{4,3,4,3,4,3},
	{5,2,5,2,5,2}
};

int golfArray[6] =
{1,2,3,4,1,2};
int golfArrayVorige[6] =
{4,1,2,3,4,1};

//array met kleuren erin voor de betreffende patronen.

// Define the output function, using pin 0 on port b.
DEFINE_WS2811_FN(WS2811RGB, PORTB, 0);
//Deze functie stuurt alle gewijzigde waarden door

int ledPositie(int led, int stripGrootte)
{
	int ledResultaat = led-((floor(led/stripGrootte))*stripGrootte);
	return ledResultaat;
} //Maakt loopen in een rij leds mogelijk

/**********************************Reset de leds************************************************/
void resetKleuren()
{
	for (int led = 0; led < 72; led++)
	{
		rgb[led].r = 0;
		rgb[led].g = 0;
		rgb[led].b = 0;
	}
	WS2811RGB(rgb, ARRAYLEN(rgb));
	//Zet alle leds weer uit
}
void resetDiscoArray()
{
	for (int secties = 0; secties < 3; secties++)
	{
		for (int xLeds = 0; xLeds < 6; xLeds++)
		{
			discoArray[xLeds][secties*2] = xLeds+1;
			discoArray[xLeds][1+(secties*2)] = 6-xLeds;
			discoArrayVorige[xLeds][secties*2] = xLeds;
			discoArrayVorige[xLeds][1+(secties*2)] = 6-xLeds+1;
		}
	}
}
void resetGolfArray()
{
	//weinig elegant
	golfArray[0] = 1;
	golfArrayVorige[0] = 4;
	golfArray[1] = 2;
	golfArrayVorige[1] = 1;
	golfArray[2] = 3;
	golfArrayVorige[2] = 2;
	golfArray[3] = 4;
	golfArrayVorige[3] = 3;
	golfArray[4] = 1;
	golfArrayVorige[4] = 4;
	golfArray[5] = 2;
	golfArrayVorige[5] = 1;
}
//Reset de arrays naar hun oorpsronkelijke staat

/**********************************BUTTON DEBOUNCING********************************************/
void ProcessPressedButton(int ButtonPressed)
{
	Pressed_Confidence_Level[ButtonPressed] ++; //extra milliseconde
	if (ButtonPressed == 1)
	{
		if (Pressed_Confidence_Level[ButtonPressed] >= 500) //halve seconde?
		{
			if (muziek == 0) //Niet op muziek?
			{
				teller++; //verschuif patroon
				if (teller == 1)
				{
					extraTeller = 180;
				}
				if (teller == 2) //huidig patroon = discoloop?
				{
					extraTeller = 0;
					resetKleuren(); //leds uit
				}
				if (teller == 3) //huidig patroon = discoloop?
				{
					resetDiscoArray();
					stappen = 0;
					resetKleuren(); //leds uit
				}
				if (teller > 3) //alles weer uit?
				{
					resetGolfArray();
					resetKleuren(); //leds uit
					stappen = 0;
					teller = 0; //teller weer op 0
				}
			}

			//stappen en sterkte variabelen resetten
			sterkte = 0;
			knopVrij = 0; //Knop is nu tijdelijk out-of-commission

			Pressed_Confidence_Level[ButtonPressed] = 0; //milliseconden naar 0
		}
	}
	if (ButtonPressed == 2)
	{
		if (Pressed_Confidence_Level[ButtonPressed] >= 500) //halve seconde?
		{
			if (muziek == 0)
			{muziek = 1;}
			else
			{muziek = 0;}
			//Wissel tussen op muziek en niet op muziek

			resetKleuren();
			resetDiscoArray();
			resetGolfArray();
			stappen = 0;
			sterkte = 0;
			//reset alles
			muziekKnopVrij = 0;
			//knop is niet vrij
			Pressed_Confidence_Level[ButtonPressed] = 0; //milliseconden naar 0
		}
	}
}

void ProcessReleasedButton(int ButtonReleased)
{
	Released_Confidence_Level[ButtonReleased] ++; //extra milliseconde
	if (Released_Confidence_Level[ButtonReleased] >= 250)
	{
		if(ButtonReleased == 1)
		{knopVrij = 1;} //knop kan weer worden ingedrukt
		if(ButtonReleased == 2)
		{muziekKnopVrij = 1;} //knop kan weer worden ingedrukt
		Released_Confidence_Level[ButtonReleased] = 0; //milliseconden naar 0
	}
}
/**********************************AD CONVERTING********************************************/
uint16_t ReadADC(uint8_t ch)
{
ch = ch&0b00000111;
//Vergelijkt ch met 00000111. De bitwise en zorgt ervoor dat ch nooit groter wordt dan *****111

ADMUX |= ch;
ADMUX |= (1<<ADLAR);
//ADLAR bit aan. Zo rekent hij van links naar rechts.

ADCSRA |= (1<<ADSC);

while(ADCSRA & (1<<ADIF));
//wacht tot de conversie klaar is.

ADCSRA|=(1<<ADIF);

return(ADCH);
//Return ADC en H omdat we alleen de bovenste 8 bits willen hebben.
}

/**********************************MAIN********************************************/
int main()
{
	// Configure pin for output.
	SET_BIT_HI(DDRB, 0);
	SET_BIT_LO(PORTB, 0);

	PORTC |= (1 << PORTC0); // PC0 pull-up aan
	PORTC |= (1 << PORTC1); // PC1 pull-up aan
	PORTC |= (1 << PORTC2); // PC2pull-up aan
	DDRC = 0; // PORTC PC0-PC7 volledig als ingang

	ADMUX = (1<<REFS0); //Referentiespanning
	ADCSRA=(1<<ADEN)|(1<<ADPS2)|(1<<ADPS1)|(1<<ADPS0);

	DDRD = 0x7f; // PORTD PD0-PD6 als uitgang
	DDRD |= (1 << PORTD0); // set LED pin PD0 to output

	TCCR0B |= (1<<CS00); //CS00 bit aan
	TIMSK0 |= (1<<TOIE0); //Overflow interrupt enable aan
	sei(); //Beginnen maar

	// Alle leds eerst uit
	for(int led = 0; led < 72; led++)
	{
		rgb[led].r = 0;
		rgb[led].g = 0;
		rgb[led].b = 0;
	}

	WS2811RGB(rgb, ARRAYLEN(rgb));
	while (1)
	{
		if (bit_is_clear(PINC, 0)) //Knop ingedrukt?
		{
			if (knopVrij == 1)
			{ProcessPressedButton(1);} //De knop is ingedrukt. Doe de dingen die je dan doet.
		}
		if (bit_is_set(PINC, 0))
		{ProcessReleasedButton(1);} //De knop is losgelaten. Doe de dingen die je dan doet.

		if (bit_is_clear(PINC, 1)) //Knop ingedrukt?
		{
			if (muziekKnopVrij == 1)
				{ProcessPressedButton(2);}
		}//De knop is ingedrukt. Doe de dingen die je dan doet.

		if (bit_is_set(PINC, 1))
		{ProcessReleasedButton(2);} //De knop is losgelaten. Doe de dingen die je dan doet.*/
	}
}

ISR(TIMER0_OVF_vect)
{
	i++; //extra tel

	if (i == 100) //genoeg tellen?
	{
		i = 0; //tellen weer op 0

		//Stroboscoop
		if(muziek == 0)
		{
			if(teller == 1)
			{
				if (extraTeller < 180 && rgb[1].r == 0)
				{extraTeller++;} //Extra tel voor het uit zijn van de stroboscoop
				else if (rgb[1].r == 0)
				{
					extraTeller = 0; //Telen op nul
					for (int sterkte = 0; sterkte < 32; sterkte++)
					{
						for (int leds = 0; leds < 72; leds++)
						{
							rgb[leds].r += 4;
							rgb[leds].g += 4;
							rgb[leds].b += 4;
							//alle waarden omhoog
						}
						WS2811RGB(rgb, ARRAYLEN(rgb));
					}
				}

				if (extraTeller < 12 && rgb[1].r > 0)
				{extraTeller++;} //Extra tel voor het aan zijn van de stroboscoop
				else if (rgb[1].r > 0)
				{
					extraTeller = 0;
					for (int sterkte = 0; sterkte < 32; sterkte++)
					{
						for (int leds = 0; leds < 72; leds++)
						{
							rgb[leds].r -= 4;
							rgb[leds].g -= 4;
							rgb[leds].b -= 4;
							//alle waarden omlaag
						}
						WS2811RGB(rgb, ARRAYLEN(rgb));
					}
				}
			}
			if (teller == 2) //disco loops
			{
				if (stappen == 0) //Leds moeten nog aan?
				{
					if (sterkte < 32)
					{
						for (int secties = 0; secties < 3; secties++) //Drie secties (twee rijen per)
						{
							for (int helften = 0; helften < 2; helften++) //Twee horizontale helften
							{
								//rij 1
								rgb[matrixArray[helften*6][secties*2]].r += 2;
								//rood
								rgb[matrixArray[1+helften*6][secties*2]].r +=2;
								rgb[matrixArray[1+helften*6][secties*2]].g +=1;
								//oranje
								rgb[matrixArray[2+helften*6][secties*2]].r +=2;
								rgb[matrixArray[2+helften*6][secties*2]].g +=2;
								//geel
								rgb[matrixArray[3+helften*6][secties*2]].g += 2;
								//groen
								rgb[matrixArray[4+helften*6][secties*2]].b += 2;
								//blauw
								rgb[matrixArray[5+helften*6][secties*2]].r += 1;
								rgb[matrixArray[5+helften*6][secties*2]].b += 2;
								//paars

								//rij 2
								rgb[matrixArray[helften*6][1+secties*2]].r += 1;
								rgb[matrixArray[helften*6][1+secties*2]].b += 2;
								//paars
								rgb[matrixArray[1+helften*6][1+secties*2]].b += 2;
								//blauw
								rgb[matrixArray[2+helften*6][1+secties*2]].g += 2;
								//groen
								rgb[matrixArray[3+helften*6][1+secties*2]].r +=2;
								rgb[matrixArray[3+helften*6][1+secties*2]].g +=2;
								//geel
								rgb[matrixArray[4+helften*6][1+secties*2]].r +=2;
								rgb[matrixArray[4+helften*6][1+secties*2]].g +=1;
								//oranje
								rgb[matrixArray[5+helften*6][1+secties]].r += 2;
								//rood
							}
						}
						sterkte++;
						WS2811RGB(rgb, ARRAYLEN(rgb));
					}
					else
					{
						stappen = 1; //leds aan
						sterkte = 32; //sterkte 32 zodat de kleuren veranderen
					}
				}
				else
				{
					if (sterkte < 32) //wordt de eerste keer gepasseerd
					{
						sterkte++;
						for (int helften = 0; helften < 2; helften++)//Twee verticale helften
						{
							for (int xLeds = 0; xLeds < 6; xLeds++)//Elke verticale rij (maal twee)
							{
								for (int yLeds = 0; yLeds < 6; yLeds++)//Elke horizontale rij
								{
									switch(discoArray[xLeds][yLeds]) //Controleer kleuren
									{
										case 1: //Rood
											rgb[matrixArray[xLeds+helften*6][yLeds]].r += 2;
											break;
										case 2: //Oranje
											rgb[matrixArray[xLeds+helften*6][yLeds]].r += 2;
											rgb[matrixArray[xLeds+helften*6][yLeds]].g += 1;
											break;
										case 3: //Geel
											rgb[matrixArray[xLeds+helften*6][yLeds]].r += 2;
											rgb[matrixArray[xLeds+helften*6][yLeds]].g += 2;
											break;
										case 4: //Groen
											rgb[matrixArray[xLeds+helften*6][yLeds]].g += 2;
											break;
										case 5: //Blauw
											rgb[matrixArray[xLeds+helften*6][yLeds]].b += 2;
											break;
										case 6: //Paars
											rgb[matrixArray[xLeds+helften*6][yLeds]].r += 1;
											rgb[matrixArray[xLeds+helften*6][yLeds]].b += 2;
											break;
									}
									switch(discoArrayVorige[xLeds][yLeds]) //Controleer voorgaande kleuren
									{
										case 1: //Rood
											rgb[matrixArray[xLeds+helften*6][yLeds]].r -= 2;
											break;
										case 2: //Oranje
											rgb[matrixArray[xLeds+helften*6][yLeds]].r -= 2;
											rgb[matrixArray[xLeds+helften*6][yLeds]].g -= 1;
											break;
										case 3: //Geel
											rgb[matrixArray[xLeds+helften*6][yLeds]].r -= 2;
											rgb[matrixArray[xLeds+helften*6][yLeds]].g -= 2;
											break;
										case 4: //Groen
											rgb[matrixArray[xLeds+helften*6][yLeds]].g -= 2;
											break;
										case 5: //Blauw
											rgb[matrixArray[xLeds+helften*6][yLeds]].b -= 2;
											break;
										case 6: //Paars
											rgb[matrixArray[xLeds+helften*6][yLeds]].r -= 1;
											rgb[matrixArray[xLeds+helften*6][yLeds]].b -= 2;
											break;
									}
									//Laat RGBwaarden niet onder 0 gaan omdat dit rare resultaten geeft.
									if (rgb[matrixArray[xLeds+helften*6][yLeds]].r < 0)
									{rgb[matrixArray[xLeds+helften*6][yLeds]].r = 0;}
									if (rgb[matrixArray[xLeds+helften*6][yLeds]].g < 0)
									{rgb[matrixArray[xLeds+helften*6][yLeds]].g =  0;}
									if (rgb[matrixArray[xLeds+helften*6][yLeds]].b < 0)
									{rgb[matrixArray[xLeds+helften*6][yLeds]].b = 0;}
								}
							}
						}
						WS2811RGB(rgb, ARRAYLEN(rgb));
					}
					else //leds al veranderd
					{
						sterkte = 0;
						for (int secties = 0; secties < 3; secties++) //elke horizontale sectie (Dit verhaal ken je nu wel)
						{
							for (int helften = 0; helften < 2; helften++) //elke verticale helft
							{
								for (int leds = 0; leds < 6; leds++) //elke verticaleale rij maal twee
								{
									discoArray[leds+(helften*6)][secties*2]++;
									discoArray[leds+(helften*6)][1+(secties*2)]--;

									//houdt de waarden binnen wat de bedoeling is
									if (discoArray[leds+(helften*6)][secties*2] > 6)
									{discoArray[leds+(helften*6)][secties*2] = 1;}
									if (discoArrayVorige[leds+(helften*6)][secties*2] > 6)
									{discoArrayVorige[leds+(helften*6)][secties*2] = 1;}

									if (discoArray[leds+(helften*6)][1+(secties*2)] < 1)
									{discoArray[leds+(helften*6)][1+(secties*2)] = 6;}
									if (discoArrayVorige[leds+(helften*6)][1+(secties*2)] < 1)
									{discoArrayVorige[leds+(helften*6)][1+(secties*2)] = 6;}
								}
							}
						}
						stappen++;
					}
				}
			}
			if (teller == 3) //golfpatroon
			{
				if (stappen == 0) //leds moeten nog aan
				{
					if (sterkte < 32)
					{
						for (int xLeds = 0; xLeds < 12; xLeds++)
						{
							//rij 1
							rgb[matrixArray[xLeds][0]].b += 2;
							//blauw
							rgb[matrixArray[xLeds][1]].g += 1;
							rgb[matrixArray[xLeds][1]].b += 2;
							//Azuur
							rgb[matrixArray[xLeds][2]].g += 1;
							rgb[matrixArray[xLeds][2]].b += 1;
							//Niet zo lichtblauw
							rgb[matrixArray[xLeds][3]].g += 2;
							rgb[matrixArray[xLeds][3]].b += 2;
							//Lichtblauw

							//rij 2
							rgb[matrixArray[xLeds][4]].b += 2;
							//blauw
							rgb[matrixArray[xLeds][5]].g += 1;
							rgb[matrixArray[xLeds][5]].b += 2;
							//Azuur
						}
						sterkte++;
						WS2811RGB(rgb, ARRAYLEN(rgb));
					}
					else
					{
						stappen = 1; //leds zijn aan
						sterkte = 32; //zodat de kleuren meteen kunnen veranderen
					}
				}
				else
				{
					if (sterkte < 32) //wordt de eerste keer gepasseerd
					{
						sterkte++;
						for (int yLeds = 0; yLeds < 6; yLeds++) //Elke horizontale rij
						{
							for (int xLeds = 0; xLeds < 12; xLeds++) //Elke veritcale rij
							{
								switch(golfArray[yLeds])
								{
									case 1: //Blauw
										rgb[matrixArray[xLeds][yLeds]].b += 2;
										break;
									case 2: //Azuur
										rgb[matrixArray[xLeds][yLeds]].b += 2;
										rgb[matrixArray[xLeds][yLeds]].g += 1;
										break;
									case 3: //Niet zo lichtblauw
										rgb[matrixArray[xLeds][yLeds]].b += 1;
										rgb[matrixArray[xLeds][yLeds]].g += 1;
										break;
									case 4: //Lichtblauw
										rgb[matrixArray[xLeds][yLeds]].b += 2;
										rgb[matrixArray[xLeds][yLeds]].g += 2;
										break;
								}
								switch(golfArrayVorige[yLeds])
								{
								case 1: //Blauw
									rgb[matrixArray[xLeds][yLeds]].b -= 2;
									break;
								case 2: //Azuur
									rgb[matrixArray[xLeds][yLeds]].b -= 2;
									rgb[matrixArray[xLeds][yLeds]].g -= 1;
									break;
								case 3: //Niet zo lichtblauw
									rgb[matrixArray[xLeds][yLeds]].b -= 1;
									rgb[matrixArray[xLeds][yLeds]].g -= 1;
									break;
								case 4: //Lichtblauw
									rgb[matrixArray[xLeds][yLeds]].b -= 2;
									rgb[matrixArray[xLeds][yLeds]].g -= 2;
									break;
								}
								if (rgb[matrixArray[xLeds][yLeds]].r < 0)
								{rgb[matrixArray[xLeds][yLeds]].r = 0;}
								if (rgb[matrixArray[xLeds][yLeds]].g < 0)
								{rgb[matrixArray[xLeds][yLeds]].g =  0;}
								if (rgb[matrixArray[xLeds][yLeds]].b < 0)
								{rgb[matrixArray[xLeds][yLeds]].b = 0;}
							}
						}
						WS2811RGB(rgb, ARRAYLEN(rgb));
					}
					else //leds zijn al veranderd
					{
						sterkte = 0;
						for (int leds = 0; leds < 6; leds++)
						{
							golfArray[leds]++;
							golfArrayVorige[leds]++;

							//houd waarden binnen de grenzen
							if (golfArray[leds] > 4)
							{golfArray[leds] = 1;}
							if (golfArrayVorige[leds] > 4)
							{golfArrayVorige[leds] = 1;}
						}
						stappen++;
					}
				}
			}
		}
		if(muziek == 1)
		{
			ADCWaarde = ReadADC(2);
			for (int leds = 0; leds < 72; leds++)
			{
				rgb[leds].r = 0;
				rgb[leds].g = 0;
				rgb[leds].b = 0;
			}
			//alle leds uit just in case.
			if (ADCWaarde < 128)
			{
				for (int leds = 0; leds < 72; leds++)
				{rgb[leds].g = 128-ADCWaarde;}
			}
			else
			{
				for (int leds = 0; leds < 72; leds++)
				{rgb[leds].r = ADCWaarde-128;}
			}
			WS2811RGB(rgb, ARRAYLEN(rgb));
			/*Als de muziek zacht is meer groen, anders meer rood*/
		}
	}
	TCNT0 = 254; //Zet count op 254 zodat elke stap de overflow triggert.
}
