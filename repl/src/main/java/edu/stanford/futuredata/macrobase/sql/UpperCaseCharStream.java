package edu.stanford.futuredata.macrobase.sql;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.misc.Interval;

/**
 * This string stream provides the lexer with upper case characters only. This greatly simplifies
 * lexing the stream, while we can maintain the original command.
 *
 * This is based on Spark org.apache.spark.sql.catalyst.parser.ParseDriver.UpperCaseCharStream
 *
 * The comment below (taken from the original class) describes the rationale for doing this:
 *
 * This class provides and implementation for a case insensitive token checker for the lexical
 * analysis part of antlr. By converting the token stream into upper case at the time when lexical
 * rules are checked, this class ensures that the lexical rules need to just match the token with
 * upper case letters as opposed to combination of upper case and lower case characters. This is
 * purely used for matching lexical rules. The actual token text is stored in the same way as the
 * user input without actually converting it into an upper case. The token values are generated by
 * the consume() function of the super class ANTLRStringStream. The LA() function is the lookahead
 * function and is purely used for matching lexical rules. This also means that the grammar will
 * only accept capitalized tokens in case it is run from other tools like antlrworks which do not
 * have the UpperCaseCharStream implementation.
 */

public class UpperCaseCharStream implements CharStream {

    private final CodePointCharStream wrapped;

    UpperCaseCharStream(final CodePointCharStream wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getText(Interval interval) {
        // ANTLR 4.7's CodePointCharStream implementations have bugs when
        // getText() is called with an empty stream, or intervals where
        // the start > end. See
        // https://github.com/antlr/antlr4/commit/ac9f7530 for one fix
        // that is not yet in a released ANTLR artifact.
        if (size() > 0 && (interval.b - interval.a >= 0)) {
            return wrapped.getText(interval);
        } else {
            return "";
        }
    }

    @Override
    public void consume() {
        wrapped.consume();
    }

    @Override
    public int LA(int i) {
        final int la = wrapped.LA(i);
        if (la == 0 || la == IntStream.EOF) {
            return la;
        }
        return Character.toUpperCase(la);
    }

    @Override
    public int mark() {
        return wrapped.mark();
    }

    @Override
    public void release(int marker) {
        wrapped.release(marker);
    }

    @Override
    public int index() {
        return wrapped.index();
    }

    @Override
    public void seek(int index) {
        wrapped.seek(index);
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public String getSourceName() {
        return wrapped.getSourceName();
    }



///**
// * The ParseErrorListener converts parse errors into AnalysisExceptions.
// */
//case object ParseErrorListener extends BaseErrorListener {
//        override def syntaxError(
//                recognizer: Recognizer[_, _],
//        offendingSymbol: scala.Any,
//                line: Int,
//                charPositionInLine: Int,
//                msg: String,
//                e: RecognitionException): Unit = {
//                val position = Origin(Some(line), Some(charPositionInLine))
//        throw new ParseException(None, msg, position, position)
//  }
//    }

    /**
     * A [[ParseException]] is an [[AnalysisException]] that is thrown during the parse process. It
     * contains fields and an extended error message that make reporting and diagnosing errors easier.
     */
//    class ParseException(
//    val command: Option[String],
//    message: String,
//    val start: Origin,
//    val stop: Origin) extends AnalysisException(message, start.line, start.startPosition) {
//
//        def this(message: String, ctx: ParserRuleContext) = {
//            this(Option(ParserUtils.command(ctx)),
//                    message,
//                    ParserUtils.position(ctx.getStart),
//                    ParserUtils.position(ctx.getStop))
//        }
//
//        override def getMessage: String = {
//                val builder = new StringBuilder
//                builder ++= "\n" ++= message
//                start match {
//        case Origin(Some(l), Some(p)) =>
//        builder ++= s"(line $l, pos $p)\n"
//        command.foreach { cmd =>
//            val (above, below) = cmd.split("\n").splitAt(l)
//            builder ++= "\n== SQL ==\n"
//            above.foreach(builder ++= _ += '\n')
//            builder ++= (0 until p).map(_ => "-").mkString("") ++= "^^^\n"
//            below.foreach(builder ++= _ += '\n')
//        }
//        case _ =>
//        command.foreach { cmd =>
//            builder ++= "\n== SQL ==\n" ++= cmd
//        }
//    }
//        builder.toString
//  }
//
//        def withCommand(cmd: String): ParseException = {
//                new ParseException(Option(cmd), message, start, stop)
//        }
//    }

/**
 * The post-processor validates & cleans-up the parse tree during the parse process.
 */
//case object PostProcessor extends SqlBaseBaseListener {
//
//        /** Remove the back ticks from an Identifier. */
//        override def exitQuotedIdentifier(ctx: SqlBaseParser.QuotedIdentifierContext): Unit = {
//                replaceTokenByIdentifier(ctx, 1) { token =>
//                // Remove the double back ticks in the string.
//                token.setText(token.getText.replace("``", "`"))
//                token
//        }
//  }
//
//        /** Treat non-reserved keywords as Identifiers. */
//        override def exitNonReserved(ctx: SqlBaseParser.NonReservedContext): Unit = {
//                replaceTokenByIdentifier(ctx, 0)(identity)
//        }
//
//    private def replaceTokenByIdentifier(
//            ctx: ParserRuleContext,
//            stripMargins: Int)(
//    f: CommonToken => CommonToken = identity): Unit = {
//        val parent = ctx.getParent
//        parent.removeLastChild()
//        val token = ctx.getChild(0).getPayload.asInstanceOf[Token]
//        val newToken = new CommonToken(
//                new org.antlr.v4.runtime.misc.Pair(token.getTokenSource, token.getInputStream),
//                SqlBaseParser.IDENTIFIER,
//                token.getChannel,
//                token.getStartIndex + stripMargins,
//                token.getStopIndex - stripMargins)
//        parent.addChild(new TerminalNodeImpl(f(newToken)))
//    }
//}
}
