package lang.flour.parser.models.codegen;

/**
 * Interface for being able to generate LLVM intermediate representation
 */
@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface Generateable {
    public String generateIR();
}
