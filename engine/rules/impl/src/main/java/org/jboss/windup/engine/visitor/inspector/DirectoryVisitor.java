package org.jboss.windup.engine.visitor.inspector;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.jboss.windup.addon.config.RulePhase;
import org.jboss.windup.engine.visitor.AbstractGraphVisitor;
import org.jboss.windup.engine.visitor.GraphVisitor;
import org.jboss.windup.graph.dao.FileResourceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryVisitor extends AbstractGraphVisitor
{
    private static final Logger LOG = LoggerFactory.getLogger(DirectoryVisitor.class);

    @Inject
    private FileResourceDao fileDao;

    @Override
    public List<Class<? extends GraphVisitor>> getDependencies()
    {
        return super.generateDependencies(BasicVisitor.class);
    }

    @Override
    public RulePhase getPhase()
    {
        return RulePhase.DISCOVERY;
    }

    @Override
    public void run()
    {

        for (org.jboss.windup.graph.model.resource.FileResourceModel file : fileDao.getAll())
        {
            visitFile(file);
        }

    }

    @Override
    public void visitFile(org.jboss.windup.graph.model.resource.FileResourceModel file)
    {
        // now, check to see whether it is a JAR, and republish the typed value.
        LOG.info(file.getFilePath());
        String filePath = file.getFilePath();
        File fileReference = new File(filePath);

        if (fileReference.isDirectory())
        {
            LOG.info("Directory: " + filePath);
            Collection<File> found = FileUtils.listFiles(fileReference, FileFileFilter.FILE, TrueFileFilter.INSTANCE);
            for (File reference : found)
            {
                org.jboss.windup.graph.model.resource.FileResourceModel graphReference = fileDao.createByFilePath(reference
                            .getAbsolutePath());
                visitFile(graphReference);
            }
        }

    }

}
