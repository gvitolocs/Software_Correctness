# JavaFX in IntelliJ – dove si trova e come configurarlo

## Dove si trova JavaFX sul tuo PC

Maven l’ha già scaricato. I jar sono qui:

```
/home/nes/.m2/repository/org/openjfx/
├── javafx-base/17.0.2/javafx-base-17.0.2-linux.jar
├── javafx-graphics/17.0.2/javafx-graphics-17.0.2-linux.jar
├── javafx-controls/17.0.2/javafx-controls-17.0.2-linux.jar
└── javafx-fxml/17.0.2/javafx-fxml-17.0.2-linux.jar
```

Non devi installare nulla: basta dire a IntelliJ di usare questi jar quando lanci l’app.

---

## Opzione A – Usare la configurazione “PLPA Graphics IDE (JavaFX)” (consigliata)

1. In alto a destra, apri il menu a tendina delle **Run configurations** (accanto al pulsante Run).
2. Seleziona **"PLPA Graphics IDE (JavaFX)"**.
3. Clicca **Run** (o Shift+F10).

Questa configurazione ha già i percorsi JavaFX impostati.

---

## Opzione B – Aggiungere JavaFX alla configurazione “Main”

Se vuoi continuare a usare la configurazione **Main**:

1. **Run → Edit Configurations…** (oppure menu a tendina → Edit Configurations…).
2. Seleziona la configurazione **Main** (o quella che usi per avviare l’app).
3. Se non la vedi, abilita **VM options**: clicca **Modify options** → **Add VM options**.
4. Nel campo **VM options** incolla (tutto su una riga):

   ```
   --module-path /home/nes/.m2/repository/org/openjfx/javafx-base/17.0.2/javafx-base-17.0.2-linux.jar:/home/nes/.m2/repository/org/openjfx/javafx-graphics/17.0.2/javafx-graphics-17.0.2-linux.jar:/home/nes/.m2/repository/org/openjfx/javafx-controls/17.0.2/javafx-controls-17.0.2-linux.jar:/home/nes/.m2/repository/org/openjfx/javafx-fxml/17.0.2/javafx-fxml-17.0.2-linux.jar --add-modules javafx.controls,javafx.fxml
   ```

5. **Apply** → **OK**.
6. Lancia di nuovo **Main**.

Da questo momento IntelliJ userà JavaFX da quella cartella quando esegue l’applicazione.
