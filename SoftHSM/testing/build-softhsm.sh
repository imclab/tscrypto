#!/usr/bin/env bash
source `dirname "$0"`/lib.sh && init || exit 1

check_if_built softhsm && exit 0
start_build softhsm

build_ok=0
case "$DISTRIBUTION" in
	openbsd )
		export AUTOCONF_VERSION="2.68"
		export AUTOMAKE_VERSION="1.11"
		append_ldflags "-L/usr/local/lib"
		;;
	sunos )
		if uname -m 2>/dev/null | $GREP -q -i sun4v 2>/dev/null; then
			ARCH_FLAG="--enable-64bit CC=\"gcc -m64\" CXX=\"g++ -m64\""
		fi
	;;
esac
case "$DISTRIBUTION" in
	centos | \
	redhat | \
	fedora | \
	sl | \
	debian | \
	opensuse )
		(
			sh autogen.sh &&
			mkdir -p build &&
			cd build &&
			../configure --prefix="$INSTALL_ROOT" \
				--with-botan=/usr &&
			$MAKE &&
			$MAKE check &&
			$MAKE install &&
			cp "softhsm.conf" "$INSTALL_ROOT/etc/softhsm.conf.build"
		) &&
		build_ok=1
		;;
	netbsd )
		(
			sh autogen.sh &&
			mkdir -p build &&
			cd build &&
			../configure --prefix="$INSTALL_ROOT" \
				--with-botan=/usr/pkg \
				--with-sqlite3=/usr/pkg &&
			$MAKE &&
			$MAKE check &&
			$MAKE install &&
			cp "softhsm.conf" "$INSTALL_ROOT/etc/softhsm.conf.build"
		) &&
		build_ok=1
		;;
	freebsd | \
	openbsd )
		(
			sh autogen.sh &&
			mkdir -p build &&
			cd build &&
			../configure --prefix="$INSTALL_ROOT" \
				--with-botan=/usr/local \
				--with-sqlite3=/usr/local &&
			$MAKE &&
			$MAKE check &&
			$MAKE install &&
			cp "softhsm.conf" "$INSTALL_ROOT/etc/softhsm.conf.build"
		) &&
		build_ok=1
		;;
	sunos )
		(
			sh autogen.sh &&
			mkdir -p build &&
			cd build &&
			eval ../configure --prefix="$INSTALL_ROOT" \
				--with-botan=/usr/local $ARCH_FLAG &&
			$MAKE &&
			$MAKE check &&
			$MAKE install &&
			cp "softhsm.conf" "$INSTALL_ROOT/etc/softhsm.conf.build"
		) &&
		build_ok=1
		;;
	suse )
		(
			sh autogen.sh &&
			mkdir -p build &&
			cd build &&
			../configure --prefix="$INSTALL_ROOT" \
				--with-botan=/usr/local &&
			$MAKE &&
			$MAKE check &&
			$MAKE install &&
			cp "softhsm.conf" "$INSTALL_ROOT/etc/softhsm.conf.build"
		) &&
		build_ok=1
		;;
	ubuntu )
		(
			sh autogen.sh &&
			mkdir -p build &&
			cd build &&
			../configure --prefix="$INSTALL_ROOT" \
				--with-botan=/usr/local &&
			$MAKE &&
			# All checks does not work on ubuntu currently (segfaults), run the checks that do
			(cd checks && ./checks -abcdei) &&
			$MAKE install &&
			cp "softhsm.conf" "$INSTALL_ROOT/etc/softhsm.conf.build"
		) &&
		build_ok=1
		;;
esac

finish

if [ "$build_ok" -eq 1 ]; then
	set_build_ok softhsm || exit 1
	exit 0
fi

exit 1
