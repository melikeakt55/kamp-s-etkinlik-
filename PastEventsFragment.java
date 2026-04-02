 @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(android.R.layout.list_content, container, false);
        listView = view.findViewById(android.R.id.list);
        db = new DBHelper(getContext());
        
        loadPastEvents();
        return view;
    }

    private void loadPastEvents() {
        eventList = new ArrayList<>();
        Cursor cursor = db.getPastEvents();
        
        if (cursor.moveToFirst()) {
            do {
                EventModel model = new EventModel(
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("date")),
                    cursor.getString(cursor.getColumnIndexOrThrow("location")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_uri"))
                );
                eventList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (getContext() != null) {
            adapter = new PastEventAdapter();
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPastEvents();
    }

    static class EventModel {
        String name, desc, date, loc, imageUri;
        EventModel(String n, String d, String t, String l, String img) {
            this.name = n; this.desc = d; this.date = t; this.loc = l; this.imageUri = img;
        }
    }

    class PastEventAdapter extends BaseAdapter {
        @Override
        public int getCount() { return eventList.size(); }
        @Override
        public Object getItem(int i) { return eventList.get(i); }
        @Override
        public long getItemId(int i) { return i; }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_event, viewGroup, false);
            }
            
            EventModel model = eventList.get(i);
            TextView name = view.findViewById(R.id.item_event_name);
            TextView date = view.findViewById(R.id.item_event_date);
            TextView loc = view.findViewById(R.id.item_event_location);
            TextView desc = view.findViewById(R.id.item_event_desc);
            ImageView img = view.findViewById(R.id.item_event_img);
            TextView statusText = view.findViewById(R.id.status_text);
            View adminPanel = view.findViewById(R.id.admin_panel);

            if (adminPanel != null) adminPanel.setVisibility(View.GONE);
            if (statusText != null) {
                statusText.setVisibility(View.VISIBLE);
                statusText.setText("BİTTİ");
                statusText.setTextColor(android.graphics.Color.GRAY);
            }

            name.setText(model.name);
            date.setText("Tarih: " + model.date);
            loc.setText("Konum: " + model.loc);
            desc.setText(model.desc);
            
            if (model.imageUri != null && !model.imageUri.isEmpty()) {
                img.setImageURI(Uri.parse(model.imageUri));
                img.setVisibility(View.VISIBLE);
            } else {
                img.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            
            return view;
        }
    }
}
