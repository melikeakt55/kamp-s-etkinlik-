 // Admin Paneli Görünürlüğü
            if (userRole.equals("admin")) {
                adminPanel.setVisibility(View.VISIBLE);
                statusText.setVisibility(View.VISIBLE);
                statusText.setText(model.isApproved == 1 ? "DURUM: ONAYLI" : "DURUM: ONAY BEKLİYOR");
                
                btnApprove.setVisibility(model.isApproved == 1 ? View.GONE : View.VISIBLE);
                
                btnApprove.setOnClickListener(v -> {
                    if (db.approveEvent(model.id)) {
                        Toast.makeText(getContext(), "Etkinlik Onaylandı", Toast.LENGTH_SHORT).show();
                        loadEvents();
                    }
                });

                btnDelete.setOnClickListener(v -> {
                    if (db.deleteEvent(model.id)) {
                        Toast.makeText(getContext(), "Etkinlik Silindi", Toast.LENGTH_SHORT).show();
                        loadEvents();
                    }
                });
            } else {
                adminPanel.setVisibility(View.GONE);
                statusText.setVisibility(View.GONE);
            }
            
            return view;
        }
    }
}
