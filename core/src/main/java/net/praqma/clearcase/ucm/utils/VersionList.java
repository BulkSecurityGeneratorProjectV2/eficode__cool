package net.praqma.clearcase.ucm.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.praqma.clearcase.ucm.entities.Version;
import net.praqma.logging.Config;
import net.praqma.util.debug.Logger;

public class VersionList extends ArrayList<Version> {
	private static java.util.logging.Logger tracer = java.util.logging.Logger.getLogger(Config.GLOBAL_LOGGER_NAME);

	private static Logger logger = Logger.getLogger();

	public VersionList() {
		tracer.entering(VersionList.class.getSimpleName(), "VersionList");

		tracer.exiting(VersionList.class.getSimpleName(), "VersionList");
	}

	public VersionList( List<Version> versions ) {
		tracer.entering(VersionList.class.getSimpleName(), "VersionList", new Object[]{versions});
		this.addAll( versions );
		tracer.exiting(VersionList.class.getSimpleName(), "VersionList");
	}

	public VersionList getLatest() {
		tracer.entering(VersionList.class.getSimpleName(), "getLatest");
		VersionList list = new VersionList();

		Map<File, Map<String, Version>> fmap = new HashMap<File, Map<String, Version>>();

		for( Version v : this ) {

			if( fmap.containsKey( v.getFile() ) ) {

				Map<String, Version> bmap = fmap.get( v.getFile() );
				if( bmap.containsKey( v.getBranch() ) ) {
					logger.debug( "Existing branch: " + v.getFile() + ", " + v.getBranch() + ", " + v.getRevision() );
					Version iv = bmap.get( v.getBranch() );
					if( iv.getRevision().intValue() < v.getRevision().intValue() ) {
						logger.debug( "Updating branch: " + v.getFile() + ", " + v.getBranch() + ", " + v.getRevision() );
						bmap.put( v.getBranch(), v );
						//fmap.put( v.getFile(), nbmap );
					}
				} else {
					logger.debug( "New branch: " + v.getFile() + ", " + v.getBranch() + ", " + v.getRevision() );
					bmap.put( v.getBranch(), v );
					//fmap.put( v.getFile(), nbmap );
				}

			} else {
				logger.debug( "New file: " + v.getFile() + ", " + v.getBranch() + ", " + v.getRevision() );
				Map<String, Version> nmap = new HashMap<String, Version>();
				nmap.put( v.getBranch(), v );
				fmap.put( v.getFile(), nmap );
			}
		}

		Set<File> keys = fmap.keySet();
		for( File file : keys ) {
			Set<String> bkeys = fmap.get( file ).keySet();
			for( String branch : bkeys ) {
				list.add( fmap.get( file ).get( branch ) );
			}
		}

		tracer.exiting(VersionList.class.getSimpleName(), "getLatest", list);
		return list;
	}
}
