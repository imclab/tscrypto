# $Id: Makefile.am 5754 2011-10-12 11:20:10Z jad $

MAINTAINERCLEANFILES = $(srcdir)/Makefile.in

AM_CPPFLAGS = 		-I$(srcdir)/../lib/cryptoki_compat \
			@BOTAN_INCLUDES@ \
			@SQLITE3_INCLUDES@ \
			@CRYPTOGRAPHIC_FRONTEND_INCLUDES@

dist_man_MANS = 	softhsm-keyconv.1
man_MANS =		softhsm.1

bin_PROGRAMS =		softhsm softhsm-keyconv

softhsm_SOURCES =	softhsm.cpp softhsm.h cryptoki.h
softhsm_LDADD =		@BOTAN_LIBS@ @SQLITE3_LIBS@ @CRYPTOGRAPHIC_FRONTEND_LIBS@ @YIELD_LIB@

softhsm_keyconv_SOURCES =	softhsm-keyconv.cpp softhsm-keyconv.h \
 				base64.c
softhsm_keyconv_LDADD =		@BOTAN_LIBS@ -lstdc++

EXTRA_DIST =		$(srcdir)/softhsm.1.in
