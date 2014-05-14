package org.jboss.windup.engine.visitor.reporter;

import javax.inject.Inject;

import org.jboss.windup.addon.config.RulePhase;
import org.jboss.windup.engine.visitor.AbstractGraphVisitor;
import org.jboss.windup.graph.dao.JarArchiveDao;
import org.jboss.windup.graph.model.resource.JarArchiveModel;
import org.jboss.windup.graph.model.resource.JavaClassModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For each JAR, list the classes it provides.
 * 
 * @author bradsdavis@gmail.com
 * 
 */
public class ArchiveProvidesReporter extends AbstractGraphVisitor
{

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveProvidesReporter.class);

    @Inject
    private JarArchiveDao jarDao;

    @Override
    public RulePhase getPhase()
    {
        return RulePhase.REPORTING;
    }

    @Override
    public void run()
    {
        for (JarArchiveModel archive : jarDao.getAll())
        {
            LOG.info("Archive: " + archive.getArchiveName());

            for (JavaClassModel clz : archive.getJavaClasses())
            {
                LOG.info(" - Provides: " + clz.getQualifiedName());
            }
        }
    }
}
