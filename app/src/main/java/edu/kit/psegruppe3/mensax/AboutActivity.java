package edu.kit.psegruppe3.mensax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);

        String mensaTimes =
                "Mittagessen:\n" +
                        "Mo. - Fr. 11:00 - 14:00 Uhr\n" +
                        "Curry Queen:\n" +
                        "Mo. - Do. 11:00 - 14:30 Uhr\n" +
                        "Fr. 11:00 - 14:00 Uhr\n" +
                        "Abendessen in der Cafeteria:\n" +
                        "Mo - Do 16:00 - 19:30 Uhr\n" +
                        "\nKontakt: Adenauerring 7, 76131 Karlsruhe\n" +
                        "Telefon: 0721 6909230\n";

        String aboutMensa = "Mensa Am Adenauerring:\n" +
                "In der Mensa Am Adenauerring werden in der Vorlesungszeit täglich um die 9.000 Essen frisch zubereitet und an acht verschiedenen Linien ausgegeben. Unsere Mitarbeiterinnen und Mitarbeiter kümmern sich in zwei Großküchen um ein schmackhaftes und abwechslungsreiches Essen sowie einen reibungslosen Ablauf mit höchsten hygienischen Standards.\n" +
                "Pasta, Pfannengerichte, Suppen, verschiedene Beilagen und Desserts (natürlich auch in der vegetarischen Variante) sind immer im Angebot. Mehrmals im Jahr runden themenspezifische Spezialitätenwochen unser Programm ab.\n" +
                "Auf Grund der hohen Nachfrage haben wir die Ausgabe des wohl beliebtesten Gerichts, Schnitzel mit Pommes und Salat, an unserer speziell entwickelten und mit modernster Technik ausgestatteten Schnitzelbar in den alten Mensasaal verlegt. Hier werden vor den Augen unserer Kunden die verschiedenen Komponenten zubereitet und wer möchte, kann sich zusätzlich am Salatbuffet bedienen.\n" +
                "Auch in den vorlesungsfreien Wochen ist die Mensa Am Adenauerring für Sie geöffnet.\n" +
                "\nUpdate\n" +
                "Straußensteaks, Garnelenspieße, Rinderfilets – Feinschmeckern und Freunden der gehobenen Küche läuft beim täglich wechselnden Speiseplan des Updates das Wasser im Munde zusammen. Im Front-Cooking-Bereich wird das Rumpsteak direkt vor Ihren Augen frisch zubereitet, ebenso wie exotisch duftende Wok-Gerichte. Köstliche Beilagen, eine umfangreiche Salatbar und leckere Dessertvariationen ergänzen das Update-Angebot. Täglich im Angebot ist unser Update-Burger zum Selbstzusammenstellen mit allen nur denkbaren Zutaten.\n" +
                "Passend zum Angebot können die Gerichte im gemütlichen Ambiente in unserem Vielmettersaal abseits des regen Mensa-Trubels eingenommen werden. Rustikale Buchenfunier-Tische und ergonomisch geformte Stühle laden hier zum Verweilen ein, der 2013 fertiggestellte Anbau lockt mit Panorama-Blick in den Mensahof und hellem Mobiliar.\n" +
                "Im Update wird die Mittagspause zum kulinarischen Erlebnis!\n" +
                "\nCurry-Queen Am Adenauerring\n" +
                "Curry Queen – World’s finest Currywurst ist der erste vom Restaurant- und Hotelführer Gault Millau ausgezeichnete „Wurst Imbiss“ mit Sitz in Hamburg und hat damit der Currywurst zu einer verdienten Renaissance verholfen.\n" +
                "Durch die Unterstützung des Studierendenwerk Karlsruhe bringt Curry Queen jetzt ihre ausgezeichneten Spezialitäten in die Mensa Am Adenauerring und lädt Sie zu einem ganz besonderen kulinarischen Erlebnis ein.\n" +
                "Genießen Sie also die Original Curry Queen Kalbscurrywurst oder ihre vegetarische Variante Veggicurrywurst mit dem hauseigenen Curry Queen Ketchup und original Belgischen Pommes Frites. Und das Besondere: Sie können aus vier verschiedenen Currys wählen: Purple Curry (ein mildes, lilafarbenes Curry mit Hibiskusblüte), Anapurna (ein kräftig, würziges Curry – der Klassiker), Curry Queen (sehr scharf mit viel Paprika) und für alle, die es richtig scharf mögen: Curry Dragon mit getrockneten pulverisierten Himbeeren und drei verschiedenen Chilisorten.\n" +
                "Lassen Sie sich von uns mitnehmen auf eine sinnliche Reise, inspiriert von kulinarischer Vielfalt und exotischen Gewürzen aus aller Welt.\n" +
                "\nCafeteria Am Adenauerring\n" +
                "Bereits ab 7.30 Uhr können Sie den Tag im Adenauerring 7 beginnen. Unsere Mitarbeiterinnen der Cafeteria Am Adenauerring bieten Ihnen durchgehend bis 17 Uhr, Freitag bis 16.00 Uhr, verschiedene Sorten Kaffee, Tee, belegte Brötchen sowie Kuchen, Eis, Obst, Joghurt und Kaltgetränke an. Ebenfalls finden Sie hier ein täglich wechselndes Angebot an heißen und kalten Snacks, wie Fleischkäsebrötchen und gefüllte Blätterteigtaschen.\n" +
                "\nAbendessen Am Adenauerring\n" +
                "Last but not least bieten wir Ihnen von Montag bis Donnerstag die Möglichkeit, zwischen 16.00 – 19.30 Uhr, in unserer Cafeteria Am Adenauerring den Tag im Studentenhaus mit einem Abendessen ausklingen zu lassen. Hierfür schlagen wir Ihnen warme Gerichte mit verschiedenen Beilagen, Salate und Desserts vor.";


        ImageView imgMensa = (ImageView) findViewById(R.id.imageView1);

        TextView txtTimes = (TextView) findViewById(R.id.txtTimes);
        txtTimes.setText(mensaTimes);

        TextView txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtAbout.setText(aboutMensa);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        } else if (id == R.id.action_search) {
            onSearchRequested();
            return true;

        } else if (id == R.id.action_about) {
            return true;

        } else if (id == R.id.action_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);

        }else if (id == R.id.action_liveCam) {
            Intent intent = new Intent(this, LiveCamsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
