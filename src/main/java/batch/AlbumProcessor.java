package batch;

import net.sf.saxon.s9api.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
public class AlbumProcessor implements ItemProcessor<Album, Album>, InitializingBean {

    static XsltExecutable xsltExecutable;
    static Processor processor;

    protected static final Log log = LogFactory.getLog(AlbumProcessor.class);

    @Override
    public Album process(Album sourceAlbum) throws Exception {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expression;

        Album destAlbum = new Album();
        destAlbum.setId(sourceAlbum.getId());

        // AUTOBOTS TRANSFORM AND ROLL OUT! :)
        String result;
        InputStream inputStream = new ByteArrayInputStream(sourceAlbum.getAlbumData().getBytes(StandardCharsets.UTF_8));
        try {
            result = transform(inputStream);
        } catch (Exception ex) {
            log.error("FATAL ERROR, ALBUM WAS SKIPPED >> ID: " + sourceAlbum.getId(), ex);
            return null;
        }
        destAlbum.setAlbumData(result);

        return destAlbum;
    }

    public static String transform(InputStream sourceXML) throws Exception {

        XsltTransformer transformer = xsltExecutable.load();
        XdmNode source = processor.newDocumentBuilder().build(new StreamSource(sourceXML));
        Serializer out = new Serializer();
        out.setOutputProperty(Serializer.Property.METHOD, "xml");
        out.setOutputProperty(Serializer.Property.INDENT, "yes");
        out.setOutputProperty(Serializer.Property.ENCODING, "UTF-8");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        out.setOutputStream(baos);
        transformer.setInitialContextNode(source);
        transformer.setDestination(out);
        transformer.transform();
        return baos.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
/*            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            URL xsltURL = classloader.getResource("transformer.xsl");
            File stylesheet = new File(xsltURL.toURI());*/

            //ClassLoader classLoader = getClass().getClassLoader();
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            File stylesheet = new File(classloader.getResource("transformer.xsl").getFile());

            processor = new Processor(false);
            XsltCompiler comp = processor.newXsltCompiler();
            xsltExecutable = comp.compile(new StreamSource(stylesheet));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
