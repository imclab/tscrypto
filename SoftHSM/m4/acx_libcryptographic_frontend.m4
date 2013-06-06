# $Id: acx_libsqlite3.m4 5753 2011-10-12 11:14:20Z jad $

AC_DEFUN([ACX_LIBCRYPTOGRAPHIC_FRONTEND],[
	
	tmp_CPPFLAGS=$CPPFLAGS
	tmp_LIBS=$LIBS
	
	AC_ARG_WITH(cryptographic_frontend,
        	AC_HELP_STRING([--with-cryptographic_frontend=PATH],[Specify prefix of path of Cryptographic Frontend]),
		[CRYPTOGRAPHIC_FRONTEND_PATH="$withval"],[])
	
	CRYPTOGRAPHIC_FRONTEND_INCLUDES="-I$CRYPTOGRAPHIC_FRONTEND_PATH/include"
        CRYPTOGRAPHIC_FRONTEND_LIBS="-L$CRYPTOGRAPHIC_FRONTEND_PATH/lib -lcryptographic_frontend"
        
        CPPFLAGS="$CPPFLAGS $CRYPTOGRAPHIC_FRONTEND_INCLUDES"
        LIBS="$LIBS $CRYPTOGRAPHIC_FRONTEND_LIBS"
        
	AC_MSG_CHECKING(what are Cryptographic Frontend includes)
	AC_MSG_RESULT($CRYPTOGRAPHIC_FRONTEND_INCLUDES)
	
	AC_MSG_CHECKING(what are Cryptographic Frontend libraries)
        AC_MSG_RESULT($CRYPTOGRAPHIC_FRONTEND_LIBS)
        
        AC_LANG_PUSH([C++])
        
        AC_LINK_IFELSE(
          [AC_LANG_PROGRAM([#include <IntegerArgument.hpp>], 
                           [using namespace cf;
                            IntegerArgument ia("", 0);])],
          [AC_MSG_RESULT([checking for CF ... yes])],
          [AC_MSG_RESULT([checking for CF ... no])
          AC_MSG_ERROR([Missing CF])]
          )
        
        AC_LANG_POP([C++])
	CPPFLAGS=$tmp_CPPFLAGS
	LIBS=$tmp_LIBS
	
	AC_SUBST(CRYPTOGRAPHIC_FRONTEND_INCLUDES)
	AC_SUBST(CRYPTOGRAPHIC_FRONTEND_LIBS)
	
	
])
