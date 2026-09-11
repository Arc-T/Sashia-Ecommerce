import { useTranslation } from "react-i18next";
import {
  Controller,
  type Control,
  type FieldPath,
  type FieldValues,
} from "react-hook-form";
import { AlertCircle, Loader2 } from "lucide-react";

import { Field, FieldContent, FieldError } from "@/components/ui/field";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { FieldHeader } from "@/components/custom/elements/form/field-header";

export interface SelectOption {
  value: string;
  label: string;
}

interface RHFSelectProps<T extends FieldValues> {
  name: FieldPath<T>;
  control: Control<T>;
  options: SelectOption[];

  valueType?: "string" | "number";

  label?: string;
  description?: string;

  disabled?: boolean;
  required?: boolean;

  isPending?: boolean;
  isError?: boolean;

  icon?: React.ReactNode;
}

export function RHFSelect<T extends FieldValues>({
  name,
  control,
  options,
  label,
  description,
  disabled,
  required,
  valueType = "number",
  isPending = false,
  isError = false,
  icon,
}: RHFSelectProps<T>) {
  const { t, i18n } = useTranslation();

  const selectDisabled = disabled || isPending || isError;

  const placeholder = isPending
    ? t("loading")
    : isError
      ? t("failed_to_fetch")
      : t("select");

  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState }) => {
        const invalid = fieldState.invalid || isError;
        console.log(field.value);
        console.log(options);
        return (
          <Field orientation="responsive" data-invalid={invalid}>
            <FieldContent>
              <FieldHeader
                label={label}
                description={description}
                icon={icon}
                required={required}
              />

              {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
            </FieldContent>

            <Select
              disabled={selectDisabled}
              value={field.value?.toString() || ""}
              onValueChange={(value) => {
                if (value === "") return;
                if (valueType === "number") {
                  field.onChange(Number(value));
                } else {
                  field.onChange(value);
                }
              }}
            >
              <SelectTrigger
                dir={i18n.dir()}
                aria-invalid={invalid}
                className="w-full"
              >
                <SelectValue placeholder={placeholder} />

                {isPending && <Loader2 className="size-4 animate-spin" />}
                {isError && <AlertCircle className="size-4 text-destructive" />}
              </SelectTrigger>

              <SelectContent dir={i18n.dir()} position="item-aligned">
                {options.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </Field>
        );
      }}
    />
  );
}
