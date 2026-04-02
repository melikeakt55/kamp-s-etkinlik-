 private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;
                            postImage.setImageURI(uri);
                            imageContainer.setVisibility(View.VISIBLE);
                            // Kalıcı izin al
                            try {
                                getContentResolver().takePersistableUriPermission(uri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postContent = findViewById(R.id.postContent);
        postImage = findViewById(R.id.postImage);
        imageContainer = findViewById(R.id.imageContainer);
        btnSelectImagePost = findViewById(R.id.btnSelectImagePost);
        btnSharePost = findViewById(R.id.btnSharePost);
        btnClose = findViewById(R.id.btnClose);
        btnRemoveImage = findViewById(R.id.btnRemoveImage);
        profileImage = findViewById(R.id.profileImage);
        
        db = new DBHelper(this);

        // Kapat butonu
        btnClose.setOnClickListener(v -> finish());

        // Görsel seçme
        btnSelectImagePost.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        // Görseli kaldır
        btnRemoveImage.setOnClickListener(v -> {
            selectedImageUri = null;
            imageContainer.setVisibility(View.GONE);
        });

        // Paylaş butonu
        btnSharePost.setOnClickListener(v -> {
            String content = postContent.getText().toString().trim();
            if (content.isEmpty() && selectedImageUri == null) {
                Toast.makeText(this, "Lütfen bir şeyler yazın", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
            String username = sharedPref.getString("username", "Anonim");
            String imageUriStr = (selectedImageUri != null) ? selectedImageUri.toString() : "";

            boolean insert = db.insertPost(username, content, imageUriStr);
            if (insert) {
                Toast.makeText(this, "Paylaşıldı!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
