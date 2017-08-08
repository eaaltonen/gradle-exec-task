import java.util.concurrent.ThreadPoolExecutor.AbortPolicy

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Try out API for adding option to always print the command line
 * for Exec task.
 */
class PrintExec extends DefaultTask {
    /**
     * Always print the command for this task. Note: for printing all
     * command line tasks, you may want to use {@code --info} or {@code -i}
     * instead.
     */
    def printCmd = false
    def whiteSpace = WhiteSpace.QUOTE
    
    // In exec
    def commandLine = []
    String executable = ''
    List<String> args = null
    
    enum WhiteSpace { QUOTE, ESCAPE }
    
    void commandLine(String... args) {
        this.commandLine = args
    }
    
    @TaskAction
    def prExec() {
        if (printCmd) {
            /* TODO should we precede the print with something?
             * bash `set -x` precedes commands with '+'
             * but a simple call to System.getenv('PS4') did not return it.
             */
            if (commandLine) {
                println "${quoteOrEscapeWhitespace(commandLine)}"
                project.exec{
                    commandLine this.commandLine
                }
            } else {
                println "${executable} ${quoteOrEscapeWhitespace(args)}"
                project.exec{
                    executable this.executable
                    args this.args
                }
            }
        }
    }
    
    def quoteOrEscapeWhitespace(String[] cmdLine) {
        return quoteOrEscapeWhitespace(Arrays.asList(cmdLine))
    }
    def quoteOrEscapeWhitespace(List<String> args) {
        // for a real implementation
        return args.join(' ')
    }
}
