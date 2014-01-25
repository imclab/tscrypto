#include <jni.h>
#include "JniSignWrapper.h"
#include <stdio.h>
#include <openssl/bn.h>


/* Returns the bytes of the result. In r_len, the output is the length */ 
static unsigned char * 
mod_pow(unsigned char * n, size_t n_len, unsigned char * e, size_t e_len, unsigned char * m, size_t m_len, size_t * r_len ) {
    BIGNUM *res = BN_new();
    BIGNUM *number = BN_new();
    BIGNUM *exponent = BN_new();
    BIGNUM *modulo = BN_new();
    BN_bin2bn(n, n_len, number);
    BN_bin2bn(e, e_len, exponent);
    BN_bin2bn(m, m_len, modulo);

    BN_CTX *bnctx = BN_CTX_new();
    BN_mod_exp(res, number, exponent, modulo, bnctx);
    
    unsigned char * r = malloc(BN_num_bytes(res));
    *r_len = BN_bn2bin(res, r);

    BN_CTX_free(bnctx);
    BN_free(modulo);
    BN_free(exponent);
    BN_free(number);
    BN_free(res);

    return r;
}

JNIEXPORT jbyteArray JNICALL 
Java_cl_niclabs_tscrypto_common_algorithms_JniSignWrapper_modPow(JNIEnv * env, jclass jc, jbyteArray num, jbyteArray expo, jbyteArray mod){
  jbyte * n = (*env)->GetByteArrayElements(env, num, 0);
  jsize n_len = (*env)->GetArrayLength(env, num);
  
  jbyte * e = (*env)->GetByteArrayElements(env, expo, 0);
  jsize e_len = (*env)->GetArrayLength(env, expo);  
  
  jbyte * m = (*env)->GetByteArrayElements(env, mod, 0);
  jsize m_len = (*env)->GetArrayLength(env, mod);

  size_t result_len = 0;
  unsigned char * result = mod_pow(n, n_len, e, e_len, m, m_len, &result_len);

  jbyteArray out = (*env)->NewByteArray(env, result_len);
  (*env)->SetByteArrayRegion(env, out, 0, result_len, result);
  free(result);

  (*env)->ReleaseByteArrayElements(env, num, n, 0);
  (*env)->ReleaseByteArrayElements(env, expo, e, 0);
  (*env)->ReleaseByteArrayElements(env, mod, m, 0);

  return out;
}

JNIEXPORT jobjectArray JNICALL Java_cl_niclabs_tscrypto_common_algorithms_JniSignWrapper_sign
  (JNIEnv *env, jclass jniSign, jstring sGroupVerifier, jstring sShareVerifier, 
                                jstring sN, jstring sX, jstring sR, jstring sDelta, jstring sSecretShare){
    FILE *file1, *file2, *file3, *file4, *file5;
    char *buff1 = (char *) malloc(8000);
    char *buff2 = (char *) malloc(8000);
    char *buff3 = (char *) malloc(8000);
    char *buff4 = (char *) malloc(8000);
    char *buff5 = (char *) malloc(8000);
    const char *cGroupVerifier, *cShareVerifier, *cN, *cX, *cR, *cDelta, *cSecretShare;
    cGroupVerifier = (*env)->GetStringUTFChars(env, sGroupVerifier , NULL );
    cShareVerifier = (*env)->GetStringUTFChars(env, sShareVerifier, NULL);
    cN = (*env)->GetStringUTFChars(env, sN, NULL);
    cX = (*env)->GetStringUTFChars(env, sX, NULL);
    cR = (*env)->GetStringUTFChars(env, sR, NULL);
    cDelta = (*env)->GetStringUTFChars(env, sDelta, NULL);
    cSecretShare = (*env)->GetStringUTFChars(env, sSecretShare, NULL);

    /*Parameters*/
    BIGNUM *groupVerifier = BN_new(), *shareVerifier = BN_new(), *n = BN_new(), *x = BN_new(), *r = BN_new(),
            *delta = BN_new(), *secretShare = BN_new();
    /*Intermediate results*/
    BIGNUM *v_prime = BN_new(), *x_tilde = BN_new(), *x_prime = BN_new(), *xi = BN_new(), *xi2n = BN_new();
    /*Aux values*/
    BIGNUM *dos = BN_new(), *deltaX2 = BN_new(), *deltaX4 = BN_new(), *secretShareX2XDelta = BN_new();
    BN_dec2bn(&groupVerifier, cGroupVerifier);
    BN_dec2bn(&shareVerifier, cShareVerifier);
    BN_dec2bn(&n, cN);
    BN_dec2bn(&x, cX);
    BN_dec2bn(&r, cR);
    BN_dec2bn(&delta, cDelta);
    BN_dec2bn(&secretShare, cSecretShare);
    BN_set_word(dos, 2ul);


    BN_CTX *bnctx;
    bnctx = BN_CTX_new();

    BN_mul(deltaX2, delta, dos, bnctx);
    BN_mul(deltaX4, deltaX2, dos, bnctx);
    BN_mul(secretShareX2XDelta, secretShare, deltaX2, bnctx);

    BN_mod_exp(v_prime, groupVerifier, r, n, bnctx);
    BN_mod_exp(x_tilde, x, deltaX4, n, bnctx);
    BN_mod_exp(x_prime, x_tilde, r, n, bnctx);
    BN_mod_exp(xi, x, secretShareX2XDelta, n, bnctx);
    BN_mod_exp(xi2n, xi, dos, n, bnctx);

    BN_CTX_free(bnctx);
    BN_free(groupVerifier);
    BN_free(shareVerifier);
    BN_free(n);
    BN_free(x);
    BN_free(r);
    BN_free(delta);
    BN_clear_free(secretShare);
    //BN_free(v_prime);
    //BN_free(x_tilde);
    //BN_free(x_prime);
    BN_free(dos);
    BN_free(deltaX2);
    BN_free(deltaX4);
    BN_free(secretShareX2XDelta);

    (*env)->ReleaseStringUTFChars(env, sGroupVerifier , NULL );
    (*env)->ReleaseStringUTFChars(env, sShareVerifier, NULL);
    (*env)->ReleaseStringUTFChars(env, sN, NULL);
    (*env)->ReleaseStringUTFChars(env, sX, NULL);
    (*env)->ReleaseStringUTFChars(env, sR, NULL);
    (*env)->ReleaseStringUTFChars(env, sDelta, NULL);
    (*env)->ReleaseStringUTFChars(env, sSecretShare, NULL);

    file1 = fmemopen(buff1, 8000, "r+");
    file2 = fmemopen(buff2, 8000, "r+");
    file3 = fmemopen(buff3, 8000, "r+");
    file4 = fmemopen(buff4, 8000, "r+");
    file5 = fmemopen(buff5, 8000, "r+");


    BN_print_fp(file1, xi);
    BN_print_fp(file2, xi2n);
    BN_print_fp(file3, x_tilde);
    BN_print_fp(file4, v_prime);
    BN_print_fp(file5, x_prime);

    BN_free(xi);
    BN_free(xi2n);
    BN_free(x_tilde);
    BN_free(v_prime);
    BN_free(x_prime);

    fclose(file1);
    fclose(file2);
    fclose(file3);
    fclose(file4);
    fclose(file5);

    jstring ret_xi = (*env)->NewStringUTF(env, buff1);
    jstring ret_xi2n = (*env)->NewStringUTF(env, buff2);
    jstring ret_x_tilde  = (*env)->NewStringUTF(env, buff3);
    jstring ret_v_prime  = (*env)->NewStringUTF(env, buff4);
    jstring ret_x_prime  =  (*env)->NewStringUTF(env, buff5);

    jobjectArray ret= (jobjectArray)(*env)->NewObjectArray(env,
                                            5,
                                            (*env)->FindClass(env, "java/lang/String"),  
                                            (*env)->NewStringUTF(env, "")); 

    (*env)->SetObjectArrayElement(env, ret, 0, (*env)->NewStringUTF(env, buff1));
    (*env)->SetObjectArrayElement(env, ret, 1, (*env)->NewStringUTF(env, buff2));
    (*env)->SetObjectArrayElement(env, ret, 2, (*env)->NewStringUTF(env, buff3));
    (*env)->SetObjectArrayElement(env, ret, 3, (*env)->NewStringUTF(env, buff4));
    (*env)->SetObjectArrayElement(env, ret, 4, (*env)->NewStringUTF(env, buff5));
    free(buff1);
    free(buff2);  
    free(buff3);
    free(buff4);
    free(buff5);
    return(ret); 


  }

