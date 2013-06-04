/* config.h.  Generated from config.h.in by configure.  */
/* config.h.in.  Generated from configure.ac by autoheader.  */

/* A bug in Botan prevents reuse of PK_Signer */
/* #undef BOTAN_NO_PK_SIGNER_REUSE */

/* Fixes an API change within Botan */
/* #undef BOTAN_PRE_1_9_10_FIX */

/* Fixes an API change within Botan */
/* #undef BOTAN_PRE_1_9_4_FIX */

/* Fixes an API problem within Botan */
/* #undef BOTAN_RESEED_FIX */

/* The default PKCS#11 library */
#define DEFAULT_PKCS11_LIB "/usr/local/lib/softhsm/libsofthsm.so"

/* The default location of softhsm.conf */
#define DEFAULT_SOFTHSM_CONF "/etc/softhsm.conf"

/* Define to 1 if you have the <dlfcn.h> header file. */
#define HAVE_DLFCN_H 1

/* Define if you have dlopen */
#define HAVE_DLOPEN 1

/* Define to 1 if you have the `getpassphrase' function. */
/* #undef HAVE_GETPASSPHRASE */

/* Define to 1 if you have the <inttypes.h> header file. */
#define HAVE_INTTYPES_H 1

/* Define to 1 if you have the `sqlite3' library (-lsqlite3). */
#define HAVE_LIBSQLITE3 1

/* Define if you have LoadLibrary */
/* #undef HAVE_LOADLIBRARY */

/* Define to 1 if you have the <memory.h> header file. */
#define HAVE_MEMORY_H 1

/* Define to 1 if you have the <pthread.h> header file. */
#define HAVE_PTHREAD_H 1

/* Define to 1 if you have the <stdint.h> header file. */
#define HAVE_STDINT_H 1

/* Define to 1 if you have the <stdlib.h> header file. */
#define HAVE_STDLIB_H 1

/* Define to 1 if you have the <strings.h> header file. */
#define HAVE_STRINGS_H 1

/* Define to 1 if you have the <string.h> header file. */
#define HAVE_STRING_H 1

/* Define to 1 if you have the <syslog.h> header file. */
#define HAVE_SYSLOG_H 1

/* Define to 1 if you have the <sys/stat.h> header file. */
#define HAVE_SYS_STAT_H 1

/* Define to 1 if you have the <sys/time.h> header file. */
#define HAVE_SYS_TIME_H 1

/* Define to 1 if you have the <sys/types.h> header file. */
#define HAVE_SYS_TYPES_H 1

/* Define to 1 if you have the <unistd.h> header file. */
#define HAVE_UNISTD_H 1

/* Define to the sub-directory in which libtool stores uninstalled libraries.
   */
#define LT_OBJDIR ".libs/"

/* Maximum PIN length */
#define MAX_PIN_LEN 255

/* Maximum number of concurrent sessions */
#define MAX_SESSION_COUNT 256

/* Minimum PIN length */
#define MIN_PIN_LEN 4

/* Name of package */
#define PACKAGE "softhsm"

/* Define to the address where bug reports for this package should be sent. */
#define PACKAGE_BUGREPORT ""

/* Define to the full name of this package. */
#define PACKAGE_NAME "SoftHSM"

/* Define to the full name and version of this package. */
#define PACKAGE_STRING "SoftHSM 1.3.4"

/* Define to the one symbol short name of this package. */
#define PACKAGE_TARNAME "softhsm"

/* Define to the home page for this package. */
#define PACKAGE_URL ""

/* Define to the version of this package. */
#define PACKAGE_VERSION "1.3.4"

/* The log level set by the user */
#define SOFTLOGLEVEL 3

/* Define to 1 if you have the ANSI C header files. */
#define STDC_HEADERS 1

/* Version number of package */
#define VERSION "1.3.4"

/* SoftHSM version number via PKCS#11 */
#define VERSION_MAJOR 1

/* SoftHSM version number via PKCS#11 */
#define VERSION_MINOR 3
