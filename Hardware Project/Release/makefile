################################################################################
# Automatically-generated file. Do not edit!
################################################################################

-include ../makefile.init

RM := rm -rf

# All of the sources participating in the build are defined here
-include sources.mk
-include subdir.mk
-include objects.mk

ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(ASM_DEPS)),)
-include $(ASM_DEPS)
endif
ifneq ($(strip $(S_DEPS)),)
-include $(S_DEPS)
endif
ifneq ($(strip $(S_UPPER_DEPS)),)
-include $(S_UPPER_DEPS)
endif
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

-include ../makefile.defs

# Add inputs and outputs from these tool invocations to the build variables 
LSS += \
IPMEDT5take2.lss \

FLASH_IMAGE += \
IPMEDT5take2.hex \

EEPROM_IMAGE += \
IPMEDT5take2.eep \

SIZEDUMMY += \
sizedummy \


# All Target
all: IPMEDT5take2.elf secondary-outputs

# Tool invocations
IPMEDT5take2.elf: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: AVR C Linker'
	avr-gcc -Wl,-Map,IPMEDT5take2.map -mmcu=atmega328 -o "IPMEDT5take2.elf" $(OBJS) $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '

IPMEDT5take2.lss: IPMEDT5take2.elf
	@echo 'Invoking: AVR Create Extended Listing'
	-avr-objdump -h -S IPMEDT5take2.elf  >"IPMEDT5take2.lss"
	@echo 'Finished building: $@'
	@echo ' '

IPMEDT5take2.hex: IPMEDT5take2.elf
	@echo 'Create Flash image (ihex format)'
	-avr-objcopy -R .eeprom -R .fuse -R .lock -R .signature -O ihex IPMEDT5take2.elf  "IPMEDT5take2.hex"
	@echo 'Finished building: $@'
	@echo ' '

IPMEDT5take2.eep: IPMEDT5take2.elf
	@echo 'Create eeprom image (ihex format)'
	-avr-objcopy -j .eeprom --no-change-warnings --change-section-lma .eeprom=0 -O ihex IPMEDT5take2.elf  "IPMEDT5take2.eep"
	@echo 'Finished building: $@'
	@echo ' '

sizedummy: IPMEDT5take2.elf
	@echo 'Invoking: Print Size'
	-avr-size --format=avr --mcu=atmega328 IPMEDT5take2.elf
	@echo 'Finished building: $@'
	@echo ' '

# Other Targets
clean:
	-$(RM) $(FLASH_IMAGE)$(ELFS)$(OBJS)$(ASM_DEPS)$(EEPROM_IMAGE)$(S_DEPS)$(SIZEDUMMY)$(S_UPPER_DEPS)$(LSS)$(C_DEPS) IPMEDT5take2.elf
	-@echo ' '

secondary-outputs: $(LSS) $(FLASH_IMAGE) $(EEPROM_IMAGE) $(SIZEDUMMY)

.PHONY: all clean dependents
.SECONDARY:

-include ../makefile.targets
