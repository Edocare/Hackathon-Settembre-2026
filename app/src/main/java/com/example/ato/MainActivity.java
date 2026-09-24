package com.example.ato;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/** Prototipo: il profilo e le bollette sono dati dimostrativi, non dati reali. */
public class MainActivity extends Activity {
    private EditText codice, domanda;
    private TextView errore, titolo, corpo, nota;
    private LinearLayout accesso, app, sezione, home, navigazione;
    private Button azionePrincipale, azioneSecondaria, chiedi;
    private ScrollView scroll;
    private String paginaPrecedente = "home";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);
        codice = findViewById(R.id.accessCodeInput); domanda = findViewById(R.id.chatQuestion);
        errore = findViewById(R.id.accessError); accesso = findViewById(R.id.accessPanel); app = findViewById(R.id.appPanel);
        home = findViewById(R.id.homePanel); sezione = findViewById(R.id.sectionPanel); navigazione = findViewById(R.id.bottomNavigation);
        titolo = findViewById(R.id.pageTitle); corpo = findViewById(R.id.pageBody); nota = findViewById(R.id.actionNote);
        azionePrincipale = findViewById(R.id.primaryAction); azioneSecondaria = findViewById(R.id.secondaryAction);
        chiedi = findViewById(R.id.chatAskButton); scroll = findViewById(R.id.rootScroll);
        findViewById(R.id.accessButton).setOnClickListener(v -> accedi());
        findViewById(R.id.navHome).setOnClickListener(v -> mostraHome());
        findViewById(R.id.navArchive).setOnClickListener(v -> mostraArchivio());
        findViewById(R.id.navAdvice).setOnClickListener(v -> mostraConsigli());
        findViewById(R.id.homeExplainButton).setOnClickListener(v -> mostraBolletta("home"));
        findViewById(R.id.homePdfButton).setOnClickListener(v -> apriPdf("bolletta_esempio_aqa.pdf"));
        findViewById(R.id.profileButton).setOnClickListener(v -> mostraProfilo());
        findViewById(R.id.backHomeButton).setOnClickListener(v -> tornaIndietro());
        azionePrincipale.setOnClickListener(v -> eseguiAzione(String.valueOf(v.getTag())));
        azioneSecondaria.setOnClickListener(v -> eseguiAzione(String.valueOf(v.getTag())));
        chiedi.setOnClickListener(v -> rispondiChat());
    }

    private void accedi() {
        if (codice.getText().toString().trim().length() != 6) { errore.setText("Inserisci tutte e 6 le cifre del codice."); return; }
        accesso.setVisibility(View.GONE); app.setVisibility(View.VISIBLE); navigazione.setVisibility(View.VISIBLE); mostraHome();
    }
    private void mostraHome() { paginaPrecedente = "home"; sezione.setVisibility(View.GONE); home.setVisibility(View.VISIBLE); vaiInAlto(); }
    private void mostraBolletta(String ritorno) {
        paginaPrecedente = ritorno;
        apriSezione("💶 Bolletta di gennaio 2025", "🟡 CONSUMO NELLA NORMA\n14 m³ questo mese · media 15 m³\n\nTOTALE: 38,71 €\n\nAcquedotto · 6,68 €\nÈ l'acqua potabile che arriva fino a casa tua.\n\nFognatura · 4,14 €\nRaccoglie l'acqua dopo l'uso.\n\nDepurazione · 11,71 €\nPulisce l'acqua prima di restituirla all'ambiente.\n\nQuota fissa · 11,00 €\nTiene sempre disponibile il servizio, anche se consumi poco.\n\nAltri costi · 5,18 €\nSono IVA e contributi previsti dalla regolazione.", "APRI / SCARICA PDF", "aqaPdf", "COME STO CONSUMANDO?", "compare");
    }
    private void mostraArchivio() {
        paginaPrecedente = "home";
        apriSezione("🗂 Archivio bollette", "Tutte le tue bollette, sempre disponibili.\n\nGennaio 2025 · 38,71 € · 🟡 14 m³\nUltima bolletta: consumo nella norma.\n\nOttobre 2024 · 42,10 € · 🔴 16 m³\nHai consumato 2 m³ in più.\n\nLuglio 2024 · 35,20 € · 🟢 11 m³\nConsumo basso: ottimo risultato!\n\nApri una bolletta per leggerla in formato semplice.", "APRI ULTIMA BOLLETTA", "latestFromArchive", "APRI BOLLETTA OTTOBRE", "oldBill");
    }
    private void mostraVecchiaBolletta() {
        paginaPrecedente = "archive";
        apriSezione("💶 Bolletta di ottobre 2024", "🔴 CONSUMO ALTO\n16 m³ questo mese · media 15 m³\n\nTOTALE: 42,10 €\n\nHai usato 2 m³ in più rispetto all'ultima bolletta. Controlla rubinetti, cassetta del WC e piccole perdite.\n\nAcquedotto · 8,10 €\nAcqua che arriva a casa tua.\n\nFognatura · 4,68 €\nRaccoglie l'acqua dopo l'uso.\n\nDepurazione · 12,32 €\nLa pulisce prima di restituirla all'ambiente.\n\nQuota fissa e altri costi · 17,00 €", "APRI PDF ESEMPIO SICAM", "sicamPdf", "VEDI CONSIGLI", "advice");
    }
    private void mostraConsigli() {
        paginaPrecedente = "home";
        apriSezione("💡 Consigli e aiuto", "Consiglio per Paolo\nHai un consumo nella norma: per migliorare ancora, scegli docce più brevi e usa lavatrice e lavastoviglie a pieno carico.\n\n🤖 Assistente AcquaChiara\nPuoi chiedermi, ad esempio:\n• Che cos'è la quota fissa?\n• Come capisco se ho una perdita?\n• Perché pago la depurazione?", "DOMANDA: COS'È LA QUOTA FISSA?", "questionFixed", "DOMANDA: HO UNA PERDITA?", "questionLeak");
        domanda.setVisibility(View.VISIBLE); chiedi.setVisibility(View.VISIBLE);
    }
    private void mostraProfilo() {
        paginaPrecedente = "home";
        apriSezione("👤 Il tuo profilo", "Paolo Rossi\nCodice utente: 123456\nUtenza domestica residente\nVia Roma 12, Mantova\nGestore: AqA\n\nQuesto è un profilo demo. Nella versione completa, il codice collega in modo sicuro le bollette e i dati della persona.", null, null, null, null);
    }
    private void mostraConfronto() {
        paginaPrecedente = "home";
        apriSezione("📊 Come stai consumando", "🟢 Basso: fino a 12 m³\n🟡 Nella norma: da 13 a 15 m³\n🔴 Alto: oltre 15 m³\n\nTu: 🟡 14 m³\nMedia personale: 15 m³\nStai consumando meno della tua media. Continua così!", null, null, null, null);
    }
    private void apriSezione(String nuovoTitolo, String nuovoCorpo, String testoUno, String tagUno, String testoDue, String tagDue) {
        home.setVisibility(View.GONE); sezione.setVisibility(View.VISIBLE); titolo.setText(nuovoTitolo); corpo.setText(nuovoCorpo); nota.setText("");
        domanda.setVisibility(View.GONE); chiedi.setVisibility(View.GONE); configuraAzione(azionePrincipale, testoUno, tagUno); configuraAzione(azioneSecondaria, testoDue, tagDue); vaiInAlto();
    }
    private void configuraAzione(Button b, String testo, String tag) { if (testo == null) { b.setVisibility(View.GONE); return; } b.setText(testo); b.setTag(tag); b.setVisibility(View.VISIBLE); }
    private void eseguiAzione(String azione) {
        if ("aqaPdf".equals(azione)) apriPdf("bolletta_esempio_aqa.pdf"); else if ("sicamPdf".equals(azione)) apriPdf("bolletta_esempio_sicam.pdf");
        else if ("latestFromArchive".equals(azione)) mostraBolletta("archive"); else if ("oldBill".equals(azione)) mostraVecchiaBolletta();
        else if ("compare".equals(azione)) mostraConfronto(); else if ("advice".equals(azione)) mostraConsigli();
        else if ("questionFixed".equals(azione)) nota.setText("🤖 La quota fissa mantiene attivo acquedotto, fognatura e depurazione, anche quando consumi poca acqua.");
        else if ("questionLeak".equals(azione)) nota.setText("🤖 Chiudi tutti i rubinetti e guarda il contatore: se continua a girare, potrebbe esserci una perdita.");
    }
    private void rispondiChat() {
        String q = domanda.getText().toString().toLowerCase();
        if (q.contains("fissa")) nota.setText("🤖 La quota fissa serve a mantenere disponibile il servizio idrico ogni giorno.");
        else if (q.contains("perdita") || q.contains("contatore")) nota.setText("🤖 Chiudi i rubinetti e controlla il contatore: se si muove ancora, chiama un tecnico.");
        else if (q.contains("depurazione")) nota.setText("🤖 La depurazione pulisce l'acqua dopo l'uso e protegge fiumi e ambiente.");
        else nota.setText("🤖 Posso rispondere alle domande su quota fissa, perdite, contatore e depurazione.");
    }
    private void tornaIndietro() { if ("archive".equals(paginaPrecedente)) mostraArchivio(); else mostraHome(); }
    private void vaiInAlto() { scroll.post(() -> scroll.smoothScrollTo(0, 0)); }
    private void apriPdf(String assetName) {
        try {
            File directory = new File(getCacheDir(), "pdfs"); if (!directory.exists() && !directory.mkdirs()) throw new IllegalStateException(); File pdf = new File(directory, assetName);
            try (InputStream input = getAssets().open(assetName); FileOutputStream output = new FileOutputStream(pdf)) { byte[] buffer = new byte[8192]; int letti; while ((letti = input.read(buffer)) != -1) output.write(buffer, 0, letti); }
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", pdf);
            startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(uri, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION));
        } catch (ActivityNotFoundException e) { nota.setText("PDF pronto: installa un lettore PDF per aprirlo."); }
        catch (Exception e) { nota.setText("Non riesco ad aprire il PDF in questo momento."); Toast.makeText(this, "Errore nell'apertura del PDF", Toast.LENGTH_SHORT).show(); }
    }
}
