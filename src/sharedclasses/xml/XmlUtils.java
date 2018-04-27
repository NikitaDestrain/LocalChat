package sharedclasses.xml;

import sharedclasses.model.Command;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtils {

    private static XmlUtils instance;

    private XmlUtils() {
    }

    public static XmlUtils getInstance() {
        if (instance == null) instance = new XmlUtils();
        return instance;
    }

    public String commandToXmlString(Command command) {
        try (StringWriter sw = new StringWriter()) {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(command, sw);
            return sw.toString();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Command parseToCommand(String xml) {
        Command command;
        try {
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xml);
            command = (Command) unmarshaller.unmarshal(sr);
            sr.close();
            return command;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
