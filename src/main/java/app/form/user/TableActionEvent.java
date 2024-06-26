package app.form.user;

public interface TableActionEvent {
    public void onEdit(int row);
    public void onDelete(int row);
    public void onPdf(int row);

}
