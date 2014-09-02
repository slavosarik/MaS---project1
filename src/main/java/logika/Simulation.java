package logika;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JTextArea;

import jsc.distributions.Exponential;

public class Simulation {

	int maxtime = 43200;
	Double simtime = 0.0;
	int pocetLiniek;
	int kapacitaKalendara = 1000;
	int pocetPokusov = 0;
	int zlyhaniaA = 0;
	int zlyhaniaB = 0;
	int pocetHovorovZA = 0;
	int pocetHovorovZB = 0;

	Exponential expTA = new Exponential(10);
	Exponential expTB = new Exponential(12);
	Exponential expTH = new Exponential(240);

	PriorityQueue<Udalost> kalendarUdalosti;
	PriorityQueue<Linka> queuePouzivaneLinky;
	Queue<Linka> queueVolneLinky;

	DecimalFormat df = new DecimalFormat("#.##");

	JTextArea area;

	public void inicializujKalendar() {
		Comparator<Udalost> udalostComparator = new UdalostComparator();
		kalendarUdalosti = new PriorityQueue<Udalost>(kapacitaKalendara,
				udalostComparator);

		Comparator<Linka> linkaComparator = new LinkaComparator();
		queuePouzivaneLinky = new PriorityQueue<Linka>(pocetLiniek,
				linkaComparator);

		queueVolneLinky = new LinkedList<Linka>();

		for (int i = 0; i < pocetLiniek; i++) {
			queueVolneLinky.add(new Linka());
		}

	}

	void naplanuj_udalost(Udalost u) {
		if (kalendarUdalosti.size() == kapacitaKalendara)
			area.append("Kalendar je plny!\n");

		kalendarUdalosti.add(u);
	}

	public void udalostVytoczAdoB() {

		// vypocet casu nasledujucej udalostiA
		double casDalsiaUdalostA = simtime + expTA.random();
		pocetPokusov++;

		// kontrola ci existuje volna linka
		if (queueVolneLinky.peek() == null) {
			zlyhaniaA++;
			// area.append("Pokus o hovor A--->B zlyhal\n");
			area.append("Cas: " + df.format(simtime)
					+ ", Pokus o hovor A--->B zlyhal\n");
		} else {
			// priebeh hovoru
			pocetHovorovZA++;
			area.append("Cas: " + df.format(simtime) + ", Hovor z A--->B\n");
			naplanuj_udalost(new Udalost("Hovor", simtime));
		}
		naplanuj_udalost(new Udalost("VytocZaDoB", casDalsiaUdalostA));

	}

	public void udalostVytoczBdoA() {

		// vypocet casu nasledujucej udalostiB
		double casDalsiaUdalostB = simtime + expTB.random();
		pocetPokusov++;

		// kontrola ci existuje volna linka
		if (queueVolneLinky.peek() == null) {
			zlyhaniaB++;
			// area.append("Pokus o hovor B--->A zlyhal\n");
			area.append("Cas: " + df.format(simtime)
					+ ", Pokus o hovor B--->A zlyhal\n");
		} else {
			// priebeh hovoru
			pocetHovorovZB++;
			area.append("Cas: " + df.format(simtime) + ", Hovor z B--->A\n");
			naplanuj_udalost(new Udalost("Hovor", simtime));
		}

		naplanuj_udalost(new Udalost("VytocZbDoA", casDalsiaUdalostB));

	}

	public void udalostHovor() {

		// priebeh hovoru
		Linka l = queueVolneLinky.remove();
		l.jeVolna = false;
		double dlzkaHovoru = expTH.random();
		naplanuj_udalost(new Udalost("KoniecHovoru", simtime + dlzkaHovoru));
		// l.casKoncaHovoru = actualTime + dlzkaHovoru;
		l.casVyuzitia += dlzkaHovoru;
		queuePouzivaneLinky.add(l);

	}

	public void udalostKoniecHovoru() {
		// koniec hovoru
		area.append("Cas: " + df.format(simtime) + ", Ukoncenie hovoru\n");
		Linka l = queuePouzivaneLinky.remove();
		l.jeVolna = true;
		queueVolneLinky.add(l);

	}

	public void ukazKalendar() {
		Object[] kalendar = kalendarUdalosti.toArray();

		area.append("Vypis kalendara\n");
		for (Object u : kalendar)
			area.append("Cas: " + df.format(((Udalost) u).time) + "; "
					+ ((Udalost) u).name + "\n");
		area.append("Koniec vypisu kalendara\n");
	}

	public void simulate(int pocetLiniek, JTextArea area) {

		area.append("Zaciatok simulacie\n\n");

		this.pocetLiniek = pocetLiniek;
		this.area = area;

		inicializujKalendar();
		naplanuj_udalost(new Udalost("VytocZaDoB", 1));
		naplanuj_udalost(new Udalost("VytocZbDoA", 1));

		Udalost u;

		while (true) {
			u = kalendarUdalosti.remove();

			if (u.time > maxtime) {
				break;
			}

			simtime = u.time;

			if (u.name == "VytocZaDoB")
				udalostVytoczAdoB();
			else if (u.name == "VytocZbDoA")
				udalostVytoczBdoA();
			else if (u.name == "Hovor")
				udalostHovor();
			else if (u.name == "KoniecHovoru")
				udalostKoniecHovoru();

		}

		// ukazKalendar();

		System.out.println("\nKoniec simulacie\n\n");

		// koniec simulacie - cakanie na ukoncenie hovorov
		while (queuePouzivaneLinky.peek() != null) {

			Linka l = queuePouzivaneLinky.remove();
			l.jeVolna = true;
			queueVolneLinky.add(l);
		}
		// vsetky linky uvolnene

		double casPouzivaniaLiniek = 0;
		for (int i = 0; i < pocetLiniek; i++) {
			casPouzivaniaLiniek += queueVolneLinky.remove().casVyuzitia;
		}

		// vypis statistik

		area.append("********************************\n");
		area.append("Statistika:\n");
		area.append("Pocet liniek: " + pocetLiniek + "\n");
		area.append("Cas simulacie: " + maxtime + "\n");
		area.append("Priemerny pocet vyuzitiych liniek: "
				+ Math.round(pocetLiniek
						* (casPouzivaniaLiniek / (simtime * pocetLiniek)))
				+ "\n");

		area.append("Priemerny cas vyuzitia 1 linky: "
				+ df.format(casPouzivaniaLiniek / pocetLiniek) + "\n");
		area.append("Percentualne vyuzitie liniek: "
				+ df.format((casPouzivaniaLiniek * 100 / (simtime * pocetLiniek)))
				+ "%\n");
		area.append("Pocet pokusov o hovor: " + pocetPokusov + "\n");

		area.append("Pocet uskutocnenych hovorov: "
				+ (pocetHovorovZA + pocetHovorovZB)
				+ ", Pocet odmietnutych hovorov: " + (zlyhaniaA + zlyhaniaB)
				+ "\n");
		area.append("Podiel uskutocnenych hovorov: "
				+ df.format(((pocetHovorovZA + pocetHovorovZB) * 100 / pocetPokusov))
				+ "%, Podiel neuspesnych hovorov: "
				+ df.format(((zlyhaniaA + zlyhaniaB) * 100 / pocetPokusov))
				+ "%\n");
		area.append("Pocet hovorov z A: " + pocetHovorovZA
				+ ", Pocet odmietnutych hovorov z A --> B: " + zlyhaniaA + "\n");
		area.append("Pocet hovorov z B: " + pocetHovorovZB
				+ ", Pocet odmietnutych hovorov z B --> A: " + zlyhaniaB + "\n");

	}
}
