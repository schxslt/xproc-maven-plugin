/*
 * Copyright 2019 by David Maus <dmaus@dmaus.name>
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package name.dmaus.xproc;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.xmlcalabash.core.XProcConfiguration;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.model.RuntimeValue;
import com.xmlcalabash.runtime.XPipeline;
import com.xmlcalabash.util.Input;

import net.sf.saxon.s9api.QName;

import java.io.File;

import java.util.Map;

@Mojo(name = "xproc")
public class Calabash extends AbstractMojo
{
    @Parameter
    private File pipeline;

    @Parameter
    private Map<String, String> options;

    public void execute () throws MojoExecutionException, MojoFailureException
    {
        XProcConfiguration config = new XProcConfiguration();
        XProcRuntime runtime = new XProcRuntime(config);

        if (pipeline == null) {
            throw new MojoFailureException("Missing configuration parameter: pipeline");
        }

        try {
            XPipeline p = runtime.load(new Input(pipeline.getAbsolutePath()));
            if (options != null) {
                for (Map.Entry<String, String> entry : options.entrySet()) {
                    p.passOption(new QName("", entry.getKey()), new RuntimeValue(entry.getValue()));
                }
            }
            p.run();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
