
;;; gcc for m6809 : Feb 15 2016 21:40:10
;;; 4.3.6 (gcc6809)
;;; ABI version 1
;;; -mint8
	.module	vec_ram_0xc8_2.c
;----- asm -----
	.bank page_c8 (BASE=0xc800,SIZE=0x0080)
	.area .dpc8 (OVR,BANK=page_c8)
	
;--- end asm ---
	.globl _Vec_Snd_shadow
	.area	.dpc8
_Vec_Snd_shadow:
	.word	0	;skip space 31
	.word	0	;skip space 29
	.word	0	;skip space 27
	.word	0	;skip space 25
	.word	0	;skip space 23
	.word	0	;skip space 21
	.word	0	;skip space 19
	.word	0	;skip space 17
	.word	0	;skip space 15
	.word	0	;skip space 13
	.word	0	;skip space 11
	.word	0	;skip space 9
	.word	0	;skip space 7
	.word	0	;skip space 5
	.word	0	;skip space 3
	.byte	0	;skip space
	.globl _Vec_Joy_Mux
_Vec_Joy_Mux:
	.word	0	;skip space 15
	.word	0	;skip space 13
	.word	0	;skip space 11
	.word	0	;skip space 9
	.word	0	;skip space 7
	.word	0	;skip space 5
	.word	0	;skip space 3
	.byte	0	;skip space
	.globl _Vec_Counters
_Vec_Counters:
	.word	0	;skip space 48
	.word	0	;skip space 46
	.word	0	;skip space 44
	.word	0	;skip space 42
	.word	0	;skip space 40
	.word	0	;skip space 38
	.word	0	;skip space 36
	.word	0	;skip space 34
	.word	0	;skip space 32
	.word	0	;skip space 30
	.word	0	;skip space 28
	.word	0	;skip space 26
	.word	0	;skip space 24
	.word	0	;skip space 22
	.word	0	;skip space 20
	.word	0	;skip space 18
	.word	0	;skip space 16
	.word	0	;skip space 14
	.word	0	;skip space 12
	.word	0	;skip space 10
	.word	0	;skip space 8
	.word	0	;skip space 6
	.word	0	;skip space 4
	.word	0	;skip space 2
	.globl _Vec_XXX_09
_Vec_XXX_09:
	.byte	0	;skip space