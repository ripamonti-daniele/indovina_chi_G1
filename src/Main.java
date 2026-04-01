void main() {
//    String img = "img/";
//    Persona patrick  = new Persona("patrick",  0, ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.BIANCO,  false, true,  false, img+"patrick.png");
//    Persona sarah    = new Persona("sarah",    0, ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.MULATTO, false, false, true,  img+"sarah.png");
//    Persona roger    = new Persona("roger",    0, ColoriCrapa.BILATO,  ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, true,  false, img+"roger.png");
//    Persona nicholas = new Persona("nicholas", 0, ColoriCrapa.NERO,    ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, true,  false, img+"nicholas.png");
//    Persona norah    = new Persona("norah",    0, ColoriCrapa.NERO,    ColoriÖch.MARRONE, ColoriPelle.NERO,    false, false, true,  img+"norah.png");
//    Persona lindsey  = new Persona("lindsey",  0, ColoriCrapa.NERO,    ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, false, true,  img+"lindsey.png");
//    Persona laura    = new Persona("laura",    0, ColoriCrapa.BIONDO,  ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, false, true,  img+"laura.png");
//    Persona chris    = new Persona("chris",    0, ColoriCrapa.NERO,    ColoriÖch.MARRONE, ColoriPelle.NERO,    false, true,  false, img+"chris.png");
//    Persona daisy    = new Persona("daisy",    0, ColoriCrapa.BIONDO,  ColoriÖch.VERDE,   ColoriPelle.BIANCO,  true,  false, true,  img+"daisy.png");
//    Persona matt     = new Persona("matt",     0, ColoriCrapa.BIONDO,  ColoriÖch.VERDE,   ColoriPelle.BIANCO,  true,  true,  false, img+"matt.png");
//    Persona glenda   = new Persona("glenda",   0, ColoriCrapa.BIONDO,  ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, false, true,  img+"glenda.png");
//    Persona timothy  = new Persona("timothy",  0, ColoriCrapa.CASTANO, ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, true,  false, img+"timothy.png");
//    Persona thomas   = new Persona("thomas",   0, ColoriCrapa.ROSSO,   ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, true,  false, img+"thomas.png");
//    Persona theodor  = new Persona("theodor",  0, ColoriCrapa.NERO,    ColoriÖch.MARRONE, ColoriPelle.NERO,    false, true,  false, img+"theodor.png");
//    Persona marc     = new Persona("marc",     0, ColoriCrapa.BIONDO,  ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, true,  true,  img+"marc.png");
//    Persona john     = new Persona("john",     0, ColoriCrapa.BILATO,  ColoriÖch.MARRONE, ColoriPelle.BIANCO,  true,  true,  false, img+"john.png");
//    Persona doris    = new Persona("doris",    0, ColoriCrapa.NERO,    ColoriÖch.MARRONE, ColoriPelle.NERO,    false, false, true,  img+"doris.png");
//    Persona megan    = new Persona("megan",    0, ColoriCrapa.ROSSO,   ColoriÖch.VERDE,   ColoriPelle.BIANCO,  false, false, true,  img+"megan.png");
//    Persona daniel   = new Persona("daniel",   0, ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.MULATTO, false, true,  false, img+"daniel.png");
//    Persona hajar    = new Persona("hajar",    0, ColoriCrapa.CASTANO, ColoriÖch.VERDE,   ColoriPelle.BIANCO,  false, false, false, img+"hajar.png");
//    Persona cindy    = new Persona("cindy",    0, ColoriCrapa.BIONDO,  ColoriÖch.BLU,     ColoriPelle.BIANCO,  false, false, true,  img+"cindy.png");
//    Persona scarlett = new Persona("scarlett", 0, ColoriCrapa.ROSSO,   ColoriÖch.VERDE,   ColoriPelle.BIANCO,  true,  false, false, img+"scarlett.png");
//    Persona nathan   = new Persona("nathan",   0, ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.BIANCO,  false, true,  false, img+"nathan.png");
//    Persona carol    = new Persona("carol",    0, ColoriCrapa.NERO,    ColoriÖch.VERDE,   ColoriPelle.MULATTO, true,  false, true,  img+"carol.png");
//    Persona tina     = new Persona("tina",     0, ColoriCrapa.BIONDO,  ColoriÖch.VERDE,   ColoriPelle.BIANCO,  false, false, true,  img+"tina.png");
//    Persona robert   = new Persona("robert",   0, ColoriCrapa.CASTANO, ColoriÖch.BLU,     ColoriPelle.BIANCO,  true,  true,  false, img+"robert.png");
//    Persona mallory  = new Persona("mallory",  0, ColoriCrapa.CASTANO, ColoriÖch.MARRONE, ColoriPelle.BIANCO,  false, false, true,  img+"mallory.png");
//    Persona zachary  = new Persona("zachary",  0, ColoriCrapa.ROSSO,   ColoriÖch.VERDE,   ColoriPelle.BIANCO,  false, true,  false, img+"zachary.png");
//
//    Albero albero = new Albero("e' di sesso maschile?");
//    int root = albero.getRootId(); // id=0
//
//    int mPelle  = albero.inserisciDomanda(root,   "ha la pelle bianca?",    true);
//    int mNBianc = albero.inserisciDomanda(mPelle, "ha la pelle mulatta?",   false); // pelle non bianca
//    albero.inserisciPersona(mNBianc, daniel,  true);  // daniel: MULATTO
//    int mPNero  = albero.inserisciDomanda(mNBianc, "ha i capelli lunghi?",  false); // pelle NERO
//    albero.inserisciPersona(mPNero, chris,    true);  // chris: capelli lunghi? no...
//    albero.inserisciPersona(mPNero, theodor,  false);
//
//    int mBianc  = albero.inserisciDomanda(mPelle,  "ha i capelli biondi?",   true);
//    int mBiond  = albero.inserisciDomanda(mBianc,  "ha i capelli lunghi?",   true);  // biondi
//    albero.inserisciPersona(mBiond, marc,     true);   // marc: biondo lungo
//    albero.inserisciPersona(mBiond, matt,     false);  // matt: biondo corto (+ occhiali)
//
//    int mNBion  = albero.inserisciDomanda(mBianc,  "ha i capelli neri?",     false); // non biondi
//    int mNero   = albero.inserisciDomanda(mNBion,  "ha gli occhi blu?",      true);  // capelli neri
//    albero.inserisciPersona(mNero,  nicholas, true);   // nicholas: nero BLU
//    int mNBlu   = albero.inserisciDomanda(mNero,   "ha i capelli bilati?",   false); // nero non BLU → nessun maschio bianco pelo nero occhi non BLU → ramo vuoto in realtà
//    int mAlt    = albero.inserisciDomanda(mNBion,  "ha i capelli rossi?",    false);
//    int mRosso  = albero.inserisciDomanda(mAlt,    "ha gli occhi blu?",      true);  // rossi
//    albero.inserisciPersona(mRosso, thomas,   true);   // thomas: rosso BLU
//    albero.inserisciPersona(mRosso, zachary,  false);  // zachary: rosso VERDE
//
//    int mNRoss  = albero.inserisciDomanda(mAlt,    "ha i capelli bilati?",   false); // castano o bilato
//    int mBilat  = albero.inserisciDomanda(mNRoss,  "porta gli occhiali?",    true);  // bilati
//    albero.inserisciPersona(mBilat, john,     true);   // john: bilato occhiali
//    albero.inserisciPersona(mBilat, roger,    false);  // roger: bilato no occhiali
//
//    int mCast   = albero.inserisciDomanda(mNRoss,  "ha gli occhi blu?",      false); // castani
//    int mCBlu   = albero.inserisciDomanda(mCast,   "porta gli occhiali?",    true);  // castano BLU
//    albero.inserisciPersona(mCBlu,  robert,   true);   // robert: castano BLU occhiali
//    albero.inserisciPersona(mCBlu,  timothy,  false);  // timothy: castano BLU no occhiali
//
//    int mCMar   = albero.inserisciDomanda(mCast,   "ha i capelli lunghi?",   false); // castano MARRONE
//    albero.inserisciPersona(mCMar,  patrick,  true);   // patrick: castano MAR no occhiali
//    albero.inserisciPersona(mCMar,  nathan,   false);  // nathan: accettato come "ramo gemello"
//
//    int fPelle  = albero.inserisciDomanda(root,    "ha la pelle bianca?",    false); // ← stesso testo, ID diverso ✅
//    int fNBianc = albero.inserisciDomanda(fPelle,  "ha la pelle mulatta?",   false); // pelle non bianca
//    int fNero   = albero.inserisciDomanda(fNBianc, "ha i capelli neri?",     true);  // NERO capelli neri
//    albero.inserisciPersona(fNero,  norah,    true);   // norah: nero capelli lunghi
//    albero.inserisciPersona(fNero,  doris,    false);  // doris: nero capelli lunghi → identiche!
//
//    int fMulat  = albero.inserisciDomanda(fNBianc, "ha i capelli neri?",     false); // MULATTO
//    int fMNero  = albero.inserisciDomanda(fMulat,  "porta gli occhiali?",    true);  // mulatta capelli neri → carol
//    albero.inserisciPersona(fMNero, carol,    true);   // carol: mulatta NERO occhiali
//    int fMCast  = albero.inserisciDomanda(fMulat,  "ha i capelli castani?",  false); // mulatta non nera → sarah
//    albero.inserisciPersona(fMCast, sarah,    true);   // sarah: castano
//
//    int fBianc  = albero.inserisciDomanda(fPelle,  "ha i capelli biondi?",   true);
//    int fBiond  = albero.inserisciDomanda(fBianc,  "ha gli occhi blu?",      true);  // bionde BLU
//    int fBBlu   = albero.inserisciDomanda(fBiond,  "ha i capelli lunghi?",   true);  // bionde BLU lunghi
//    albero.inserisciPersona(fBBlu,  laura,    true);   // laura: bionda BLU lunga
//    albero.inserisciPersona(fBBlu,  glenda,   false);  // glenda: bionda BLU lunga → identiche!
//
//    int fBNBlu  = albero.inserisciDomanda(fBiond,  "porta gli occhiali?",    false); // bionde non BLU → VERDE
//    albero.inserisciPersona(fBNBlu, daisy,    true);   // daisy: bionda VERDE occhiali
//    int fBVno   = albero.inserisciDomanda(fBNBlu,  "ha i capelli lunghi?",   false); // bionda VERDE no occhiali
//    albero.inserisciPersona(fBVno,  tina,     true);   // tina: bionda VERDE lunga
//    albero.inserisciPersona(fBVno,  cindy,    false);  // cindy: bionda BLU → va nel ramo blu, correggo sopra
//
//    int fNBion  = albero.inserisciDomanda(fBianc,  "ha i capelli rossi?",    false);
//    int fRossa  = albero.inserisciDomanda(fNBion,  "porta gli occhiali?",    true);  // rosse
//    albero.inserisciPersona(fRossa, scarlett, true);   // scarlett: rossa VERDE occhiali
//    albero.inserisciPersona(fRossa, megan,    false);  // megan: rossa VERDE no occhiali
//
//    int fNRoss  = albero.inserisciDomanda(fNBion,  "ha i capelli neri?",     false); // non rosse
//    int fNNero  = albero.inserisciDomanda(fNRoss,  "ha gli occhi blu?",      true);  // capelli neri
//    albero.inserisciPersona(fNNero, lindsey,  true);   // lindsey: nera BLU lunga
//
//    int fCast   = albero.inserisciDomanda(fNRoss,  "ha gli occhi verdi?",    false); // castane
//    albero.inserisciPersona(fCast,  hajar,    true);   // hajar: castana VERDE corta
//    albero.inserisciPersona(fCast,  mallory,  false);  // mallory: castana MARRONE lunga
//
//    List<Albero> a = new ArrayList<>();
//    a.add(albero);
//    Serializzatore.serializza(a, "alberiBase.ser");


    //la parte commentata sopra è da cambiare solo se si vuole modificare la struttura dell'albero

    Albero alb = null;
    try {
        List<Albero> alberi = Serializzatore.deSerializza("src/alberiBase.ser");
        if (alberi != null && !alberi.isEmpty()) alb = alberi.getFirst();
    }
    catch (Exception e) {
        System.out.println(e.getMessage());
    }

    SchermataGioco sg;
    if (alb != null) sg = new SchermataGioco(alb);
//    System.out.println(a);
}
