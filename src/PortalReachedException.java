public class PortalReachedException extends RuntimeException {
    public PortalReachedException() {
        super("Ai trecut prin portal! Se regenereaza harta.");
    }
}