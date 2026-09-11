import { useTranslation } from "react-i18next";
import {
  Controller,
  type Control,
  type FieldPath,
  type FieldValues,
} from "react-hook-form";

import { Field, FieldError } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { FieldHeader } from "@/components/custom/elements/form/field-header";

interface RHFInputProps<T extends FieldValues> {
  name: FieldPath<T>;
  control: Control<T>;

  label?: string;
  description?: string;
  placeholder?: string;

  type?: React.InputHTMLAttributes<HTMLInputElement>["type"];
  autoComplete?: string;
  props?: React.ComponentProps<"input">;
  disabled?: boolean;
  required?: boolean;

  icon?: React.ReactNode;
  inputClassName?: string;
}

export function RHFInput<T extends FieldValues>({
  name,
  control,
  label,
  description,
  placeholder,
  type = "text",
  autoComplete = "off",
  disabled,
  required,
  icon,
  props,
  inputClassName,
}: RHFInputProps<T>) {
  const { t, i18n } = useTranslation();

  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState: { invalid, error } }) => (
        <Field data-invalid={invalid}>
          <FieldHeader
            label={label}
            description={description}
            icon={icon}
            required={required}
          />

          <Input
            {...field}
            dir={i18n.dir()}
            type={type}
            disabled={disabled}
            autoComplete={autoComplete}
            aria-invalid={invalid}
            placeholder={placeholder ? t(placeholder) : undefined}
            className={inputClassName}
            {...props}
          />

          {invalid && error && <FieldError errors={[error]} />}
        </Field>
      )}
    />
  );
}
