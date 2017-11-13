public class Bottom {
    public Power_interface device;

    Bottom(Power_interface device)
    {
        this.device = device;
    }

    public void TouchBottom()
    {
        System.out.println("На кнопку нажали");
        if (device != null)
            device.Set_power();
        else System.out.println("Ничего не произошло...");
    }
}
