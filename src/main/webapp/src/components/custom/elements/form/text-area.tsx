import {
  Controller,
  type Control,
  type FieldPath,
  type FieldValues,
} from "react-hook-form";
import { useTranslation } from "react-i18next";

import { Field, FieldError } from "@/components/ui/field";
import { Textarea } from "@/components/ui/textarea";
import { FieldHeader } from "@/components/custom/elements/form/field-header";

interface RHFTextareaProps<T extends FieldValues> {
  name: FieldPath<T>;
  control: Control<T>;

  label?: string;
  description?: string;
  placeholder?: string;
  icon?: React.ReactNode;

  disabled?: boolean;
  required?: boolean;
  rows?: number;
}

export function RHFTextarea<T extends FieldValues>({
  name,
  control,
  label,
  description,
  placeholder,
  icon,
  disabled,
  required,
  rows = 4,
}: RHFTextareaProps<T>) {
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

          <Textarea
            {...field}
            dir={i18n.dir()}
            rows={rows}
            disabled={disabled}
            aria-invalid={invalid}
            placeholder={placeholder ? t(placeholder) : undefined}
          />

          {invalid && <FieldError errors={[error]} />}
        </Field>
      )}
    />
  );
}
