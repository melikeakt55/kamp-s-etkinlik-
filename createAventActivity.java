@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventName = findViewById(R.id.eventName);
        eventDesc = findViewById(R.id.eventDesc);
        eventDate = findViewById(R.id.eventDate);
        eventLocation = findViewById(R.id.eventLocation);
        saveBtn = findViewById(R.id.saveBtn);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgEvent = findViewById(R.id.imgEvent);
        db = new DBHelper(this);

        btnSelectImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        eventDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                        eventDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        saveBtn.setOnClickListener(v -> {
            String name = eventName.getText().toString().trim();
            String desc = eventDesc.getText().toString().trim();
            String date = eventDate.getText().toString().trim();
            String loc = eventLocation.getText().toString().trim();
            String imageUriStr = (selectedImageUri != null) ? selectedImageUri.toString() : "";

            if (name.isEmpty() || desc.isEmpty() || date.isEmpty() || loc.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase database = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("description", desc);
            values.put("date", date);
            values.put("location", loc);
            values.put("image_uri", imageUriStr);
            values.put("owner_id", 1); 

            long result = database.insert("events", null, values);
            if (result != -1) {
                Toast.makeText(this, "Etkinlik başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
