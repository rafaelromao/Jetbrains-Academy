class ComputerFacadeTestDrive {
    public static void main(String[] args) {
        var monitor = new Monitor();
        var processor = new Processor();
        var keyboard = new Keyboard();

        ComputerFacade computerFacade = new ComputerFacade(monitor, processor, keyboard);

        computerFacade.turnOnComputer();
        computerFacade.turnOffComputer();
    }
}

class ComputerFacade {
    private Monitor monitor;
    private Processor processor;
    private Keyboard keyboard;


    public ComputerFacade(Monitor monitor, Processor processor, Keyboard keyboard) {
        this.monitor = monitor;
        this.processor = processor;
        this.keyboard = keyboard;
    }

    public void turnOnComputer() {
        processor.on();
        monitor.on();
        keyboard.on();
    }

    public void turnOffComputer() {
        keyboard.off();
        monitor.off();
        processor.off();
    }
}

class Processor {
    private String description = "Processor";

    public void on() {
        System.out.println(description + " on");
    }

    public void off() {
        System.out.println(description + " off");
    }
}

class Monitor {
    private String description = "Monitor";

    public void on() {
        System.out.println(description + " on");
    }

    public void off() {
        System.out.println(description + " off");
    }
}

class Keyboard {
    private String description = "Keyboard";

    public void on() {
        System.out.println(description + " on");
        turnOnBacklight();
    }

    public void off() {
        System.out.println(description + " off");
        turnOffBacklight();
    }

    private void turnOnBacklight() {
        System.out.println("Backlight is turned on");
    }

    private void turnOffBacklight() {
        System.out.println("Backlight is turned off");
    }
}