package i2p.bote.web;

import i2p.bote.I2PBote;
import i2p.bote.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.i2p.util.Log;

public class MessageTag extends BodyTagSupport {
    private static final long serialVersionUID = 2446806168091763863L;
    private static final String REQUEST_SCOPE = "request";
    private static final String PAGE_SCOPE = "page";
    private static final String SESSION_SCOPE = "session";
    private static final String APPLICATION_SCOPE = "application";
    
    private Log log = new Log(MessageTag.class);
    private String key;
    private String bundle;
    private String var;
    private int scope = PageContext.PAGE_SCOPE;
    private boolean hide;
    private List<String> parameters = new ArrayList<String>();   // holds values specified in <ib:param> tags
    private PageContext pageContext;

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }
    
    public int doEndTag() throws JspException {
        String translation;
        if (hide && I2PBote.getInstance().getConfiguration().getHideLocale())
            translation = key;
        else
            translation = Util._(key);
        
        // replace {0}, {1},... with param values
        do {
            int curlyStart = translation.indexOf('{');
            int curlyEnd = translation.indexOf('}', curlyStart);
            if (curlyStart<0 || curlyEnd<0)
                break;
            String indexStr = translation.substring(curlyStart+1, curlyEnd);
            try {
                int index = Integer.valueOf(indexStr);
                if (parameters.size() <= index)
                    log.error("Parameter #" + index + " doesn't exist for message key <" + key + ">.");
                else
                    translation = translation.substring(0, curlyStart) + parameters.get(index) + translation.substring(curlyEnd + 1);
            }
            catch (NumberFormatException e) {
                log.error("Expected an int, got <" + indexStr + "> for a parameter index; message key: <" + key + ">.");
            }
        } while (true);
        
        // write the translated string to the page or into a variable
        if (var != null)
            pageContext.setAttribute(var, translation, scope);
        else
            try {
                pageContext.getOut().println(translation);
            } catch (IOException e) {
                throw new JspException(e);
            }
            
        return EVAL_PAGE;
    }
    
    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getBundle() {
        return bundle;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public void setScope(String scope) {
        if (REQUEST_SCOPE.equalsIgnoreCase(scope))
            this.scope = PageContext.REQUEST_SCOPE;
        else if (PAGE_SCOPE.equalsIgnoreCase(scope))
            this.scope = PageContext.PAGE_SCOPE;
        else if (SESSION_SCOPE.equalsIgnoreCase(scope))
            this.scope = PageContext.SESSION_SCOPE;
        else if (APPLICATION_SCOPE.equalsIgnoreCase(scope))
            this.scope = PageContext.APPLICATION_SCOPE;
        else
            this.scope = PageContext.PAGE_SCOPE;
    }

    public String getScope() {
        return Integer.valueOf(scope).toString();
    }
    
    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isHide() {
        return hide;
    }

    void addParameter(String param) {
        parameters.add(param);
    }
}